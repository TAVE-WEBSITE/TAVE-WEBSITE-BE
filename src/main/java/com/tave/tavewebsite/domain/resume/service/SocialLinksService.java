package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.PortfolioUploadResponseDto;
import com.tave.tavewebsite.domain.resume.dto.response.SocialLinksResponseDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.InvalidDataOwnerException;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.domain.resume.validator.FileValidator;
import com.tave.tavewebsite.global.s3.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Service
public class SocialLinksService {

    private final ResumeRepository resumeRepository;
    private final S3Service s3Service;
    private final FileValidator fileValidator;

    public SocialLinksService(ResumeRepository resumeRepository, S3Service s3Service, FileValidator fileValidator) {
        this.resumeRepository = resumeRepository;
        this.s3Service = s3Service;
        this.fileValidator = fileValidator;
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
