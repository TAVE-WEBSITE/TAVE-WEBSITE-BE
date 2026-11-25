package com.tave.tavewebsite.domain.resume.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.tave.tavewebsite.domain.resume.dto.request.PortfolioPresignedUrlRequestDto;
import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.PortfolioPresignedUrlResponseDto;
import com.tave.tavewebsite.domain.resume.dto.response.PortfolioUploadResponseDto;
import com.tave.tavewebsite.domain.resume.dto.response.SocialLinksResponseDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.FileSizeExceededException;
import com.tave.tavewebsite.domain.resume.exception.InvalidDataOwnerException;
import com.tave.tavewebsite.domain.resume.exception.PdfFileTypeException;
import com.tave.tavewebsite.domain.resume.exception.PortfolioObjectNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.PortfolioUploadInProgressException;
import com.tave.tavewebsite.domain.resume.exception.PortfolioUploadNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.domain.resume.validator.FileValidator;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import com.tave.tavewebsite.global.s3.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Service
public class SocialLinksService {

    private static final String PORTFOLIO_KEY_PREFIX = "portfolio/";
    private static final String UPLOAD_TICKET_PREFIX = "portfolio:upload:";
    private static final String UPLOAD_LOCK_PREFIX = "lock:portfolio:";
    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofMinutes(10);
    // 만료 직전 시작한 대용량 업로드도 완료 처리할 수 있도록 URL 만료보다 길게 유지
    private static final int UPLOAD_TICKET_TTL_MINUTES = 30;
    private static final long LOCK_WAIT_SECONDS = 3;
    private static final long LOCK_LEASE_SECONDS = 30;

    private final ResumeRepository resumeRepository;
    private final S3Service s3Service;
    private final FileValidator fileValidator;
    private final RedisUtil redisUtil;
    private final RedissonClient redissonClient;

    public SocialLinksService(ResumeRepository resumeRepository, S3Service s3Service, FileValidator fileValidator,
                              RedisUtil redisUtil, RedissonClient redissonClient) {
        this.resumeRepository = resumeRepository;
        this.s3Service = s3Service;
        this.fileValidator = fileValidator;
        this.redisUtil = redisUtil;
        this.redissonClient = redissonClient;
    }

    // 소셜 주소 등록
    @Transactional
    public void createSocialLinks(Long resumeId, SocialLinksRequestDto dto, Long memberId) {
        Resume resume = getAuthorizedResume(resumeId, memberId);
        resume.updateSocialLinks(dto);
    }

    public SocialLinksResponseDto getSocialLinks(Long resumeId, Long memberId) {
        Resume resume = getAuthorizedResume(resumeId, memberId);
        return new SocialLinksResponseDto(
                memberId,
                resume.getBlogUrl(),
                resume.getGithubUrl(),
                resume.getPortfolioUrl()
        );
    }

    // 소셜 링크 업데이트
    @Transactional
    public void updateSocialLinks(Long resumeId, SocialLinksRequestDto dto, Long memberId) {
        Resume resume = getAuthorizedResume(resumeId, memberId);
        resume.updateSocialLinks(dto);
    }

    /**
     * @deprecated PresignedUrl 방식(issuePortfolioPresignedUrl, completePortfolioUpload)으로 대체
     */
    @Deprecated
    @Transactional
    public PortfolioUploadResponseDto updatePortfolio(Long resumeId, MultipartFile file, Long memberId) {
        Resume resume = getAuthorizedResume(resumeId, memberId);

        String existingUrl = resume.getPortfolioUrl();

        // 기존 파일 삭제
        if (existingUrl != null && !existingUrl.isEmpty()) {
            s3Service.deleteFileByUrl(existingUrl);
        }

        if (file == null) {
            // null 업로드 처리 (삭제)
            resume.updatePortfolio(null);
            resumeRepository.save(resume);
            return new PortfolioUploadResponseDto(null);
        }

        // 파일 크기 & 확장자 검증
        fileValidator.validateSize(file);
        URL portfolioUrl = s3Service.uploadFile(file);

        resume.updatePortfolio(portfolioUrl.toString());
        resumeRepository.save(resume);
        return new PortfolioUploadResponseDto(portfolioUrl.toString());
    }

    // 포트폴리오 업로드용 PresignedUrl 발급
    public PortfolioPresignedUrlResponseDto issuePortfolioPresignedUrl(Long resumeId,
                                                                       PortfolioPresignedUrlRequestDto dto,
                                                                       Long memberId) {
        getAuthorizedResume(resumeId, memberId);
        fileValidator.validatePdfContentType(dto.contentType());
        fileValidator.validateSize(dto.fileSize());

        String objectKey = PORTFOLIO_KEY_PREFIX + UUID.randomUUID() + ".pdf";
        URL presignedUrl = s3Service.generatePresignedPutUrl(
                objectKey, FileValidator.PDF_CONTENT_TYPE, PRESIGNED_URL_EXPIRATION);

        redisUtil.set(UPLOAD_TICKET_PREFIX + objectKey, toTicketValue(memberId, resumeId), UPLOAD_TICKET_TTL_MINUTES);
        return new PortfolioPresignedUrlResponseDto(presignedUrl.toString(), objectKey);
    }

    // 업로드 완료 처리: 발급 정보·S3 객체 검증 후 URL 저장, 기존 객체는 저장 성공 후 삭제
    public PortfolioUploadResponseDto completePortfolioUpload(Long resumeId, String objectKey, Long memberId) {
        if (objectKey == null || !objectKey.startsWith(PORTFOLIO_KEY_PREFIX)) {
            throw new PortfolioUploadNotFoundException();
        }

        return executeWithPortfolioLock(resumeId, () -> {
            Resume resume = getAuthorizedResume(resumeId, memberId);
            String ticketKey = UPLOAD_TICKET_PREFIX + objectKey;
            String newUrl = s3Service.getImageUrl(objectKey).toString();

            // 이미 완료된 요청의 재시도는 그대로 성공 응답
            if (newUrl.equals(resume.getPortfolioUrl())) {
                redisUtil.deleteRedisKey(ticketKey);
                return new PortfolioUploadResponseDto(newUrl);
            }

            validateUploadTicket(ticketKey, memberId, resumeId);
            validateUploadedObject(objectKey, ticketKey);
            s3Service.makePublicRead(objectKey);

            String oldUrl = resume.getPortfolioUrl();
            resume.updatePortfolio(newUrl);
            resumeRepository.save(resume);

            redisUtil.deleteRedisKey(ticketKey);
            deleteS3ObjectQuietly(oldUrl);
            return new PortfolioUploadResponseDto(newUrl);
        });
    }

    // 포트폴리오 삭제: DB 반영 후 S3 객체 삭제
    public void deletePortfolio(Long resumeId, Long memberId) {
        executeWithPortfolioLock(resumeId, () -> {
            Resume resume = getAuthorizedResume(resumeId, memberId);
            String oldUrl = resume.getPortfolioUrl();
            if (oldUrl == null || oldUrl.isEmpty()) {
                return null;
            }

            resume.updatePortfolio(null);
            resumeRepository.save(resume);

            deleteS3ObjectQuietly(oldUrl);
            return null;
        });
    }

    private void validateUploadTicket(String ticketKey, Long memberId, Long resumeId) {
        Object ticket = redisUtil.get(ticketKey);
        if (ticket == null) {
            throw new PortfolioUploadNotFoundException();
        }
        if (!toTicketValue(memberId, resumeId).equals(ticket.toString())) {
            throw new InvalidDataOwnerException();
        }
    }

    // 실제 업로드된 객체의 존재·크기·Content-Type 검증, 실패 시 객체와 발급 정보 정리
    private void validateUploadedObject(String objectKey, String ticketKey) {
        ObjectMetadata metadata = s3Service.getObjectMetadataOrNull(objectKey);
        if (metadata == null) {
            throw new PortfolioObjectNotFoundException();
        }

        try {
            fileValidator.validateSize(metadata.getContentLength());
            fileValidator.validatePdfContentType(metadata.getContentType());
        } catch (FileSizeExceededException | PdfFileTypeException e) {
            deleteS3ObjectQuietly(s3Service.getImageUrl(objectKey).toString());
            redisUtil.deleteRedisKey(ticketKey);
            throw e;
        }
    }

    private <T> T executeWithPortfolioLock(Long resumeId, Supplier<T> task) {
        RLock lock = redissonClient.getLock(UPLOAD_LOCK_PREFIX + resumeId);
        boolean acquired;
        try {
            acquired = lock.tryLock(LOCK_WAIT_SECONDS, LOCK_LEASE_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PortfolioUploadInProgressException();
        }
        if (!acquired) {
            throw new PortfolioUploadInProgressException();
        }

        try {
            return task.get();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    // 기존 객체 삭제 실패는 새 포트폴리오 저장 결과에 영향을 주지 않도록 로그만 남김
    private void deleteS3ObjectQuietly(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        try {
            s3Service.deleteFileByUrl(fileUrl);
        } catch (Exception e) {
            log.error("[Portfolio] 기존 S3 객체 삭제 실패 - url: {}", fileUrl, e);
        }
    }

    private String toTicketValue(Long memberId, Long resumeId) {
        return memberId + ":" + resumeId;
    }

    // Resume 조회 및 소유자 검증을 함께 수행하는 메서드
    private Resume getAuthorizedResume(Long resumeId, Long memberId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        if (!resume.getMember().getId().equals(memberId)) {
            throw new InvalidDataOwnerException();
        }
        return resume;
    }

}
