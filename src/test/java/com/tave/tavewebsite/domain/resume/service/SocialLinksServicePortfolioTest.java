package com.tave.tavewebsite.domain.resume.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.request.PortfolioPresignedUrlRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.PortfolioPresignedUrlResponseDto;
import com.tave.tavewebsite.domain.resume.dto.response.PortfolioUploadResponseDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.FileSizeExceededException;
import com.tave.tavewebsite.domain.resume.exception.InvalidDataOwnerException;
import com.tave.tavewebsite.domain.resume.exception.PdfFileTypeException;
import com.tave.tavewebsite.domain.resume.exception.PortfolioObjectNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.PortfolioUploadNotFoundException;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.domain.resume.validator.FileValidator;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import com.tave.tavewebsite.global.s3.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.net.URL;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SocialLinksServicePortfolioTest {

    private static final Long RESUME_ID = 1L;
    private static final Long MEMBER_ID = 10L;
    private static final String BASE = "https://bucket.s3.ap-northeast-2.amazonaws.com/";
    private static final String KEY = "portfolio/new.pdf";
    private static final String TICKET = "portfolio:upload:" + KEY;

    private ResumeRepository resumeRepository;
    private S3Service s3Service;
    private RedisUtil redisUtil;
    private SocialLinksService service;
    private Resume resume;

    @BeforeEach
    void setUp() throws Exception {
        resumeRepository = mock(ResumeRepository.class);
        s3Service = mock(S3Service.class);
        redisUtil = mock(RedisUtil.class);
        RedissonClient redissonClient = mock(RedissonClient.class);
        RLock lock = mock(RLock.class);
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);

        service = new SocialLinksService(resumeRepository, s3Service, new FileValidator(), redisUtil, redissonClient);

        Member member = mock(Member.class);
        when(member.getId()).thenReturn(MEMBER_ID);
        resume = mock(Resume.class);
        when(resume.getMember()).thenReturn(member);
        when(resumeRepository.findById(RESUME_ID)).thenReturn(Optional.of(resume));
        when(s3Service.getImageUrl(anyString()))
                .thenAnswer(inv -> new URL(BASE + inv.getArgument(0, String.class)));
    }

    private ObjectMetadata metadata(long size, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(size);
        metadata.setContentType(contentType);
        return metadata;
    }

    @Test
    void 발급_시_PDF가_아니면_예외() {
        assertThatThrownBy(() -> service.issuePortfolioPresignedUrl(
                RESUME_ID, new PortfolioPresignedUrlRequestDto("image/png", 100L), MEMBER_ID))
                .isInstanceOf(PdfFileTypeException.class);
    }

    @Test
    void 발급_시_200MB_초과면_예외() {
        assertThatThrownBy(() -> service.issuePortfolioPresignedUrl(
                RESUME_ID, new PortfolioPresignedUrlRequestDto("application/pdf", 200L * 1024 * 1024 + 1), MEMBER_ID))
                .isInstanceOf(FileSizeExceededException.class);
    }

    @Test
    void 발급_성공_시_발급정보를_Redis에_저장() throws Exception {
        when(s3Service.generatePresignedPutUrl(anyString(), eq("application/pdf"), any()))
                .thenReturn(new URL("https://presigned"));

        PortfolioPresignedUrlResponseDto res = service.issuePortfolioPresignedUrl(
                RESUME_ID, new PortfolioPresignedUrlRequestDto("application/pdf", 1024L), MEMBER_ID);

        assertThat(res.objectKey()).startsWith("portfolio/").endsWith(".pdf");
        verify(redisUtil).set(eq("portfolio:upload:" + res.objectKey()), eq("10:1"), anyInt());
    }

    @Test
    void 완료_시_발급정보가_없으면_예외() {
        when(redisUtil.get(TICKET)).thenReturn(null);

        assertThatThrownBy(() -> service.completePortfolioUpload(RESUME_ID, KEY, MEMBER_ID))
                .isInstanceOf(PortfolioUploadNotFoundException.class);
    }

    @Test
    void 완료_시_발급_당시_지원서와_다르면_예외() {
        when(redisUtil.get(TICKET)).thenReturn("10:2");

        assertThatThrownBy(() -> service.completePortfolioUpload(RESUME_ID, KEY, MEMBER_ID))
                .isInstanceOf(InvalidDataOwnerException.class);
    }

    @Test
    void 완료_시_S3_객체가_없으면_예외() {
        when(redisUtil.get(TICKET)).thenReturn("10:1");
        when(s3Service.getObjectMetadataOrNull(KEY)).thenReturn(null);

        assertThatThrownBy(() -> service.completePortfolioUpload(RESUME_ID, KEY, MEMBER_ID))
                .isInstanceOf(PortfolioObjectNotFoundException.class);
    }

    @Test
    void 완료_시_ContentType이_틀리면_객체를_삭제하고_예외() {
        when(redisUtil.get(TICKET)).thenReturn("10:1");
        when(s3Service.getObjectMetadataOrNull(KEY)).thenReturn(metadata(1024, "image/png"));

        assertThatThrownBy(() -> service.completePortfolioUpload(RESUME_ID, KEY, MEMBER_ID))
                .isInstanceOf(PdfFileTypeException.class);
        verify(s3Service).deleteFileByUrl(BASE + KEY);
        verify(resume, never()).updatePortfolio(any());
    }

    @Test
    void 완료_성공_시_URL_저장_후_기존_객체_삭제() {
        when(redisUtil.get(TICKET)).thenReturn("10:1");
        when(s3Service.getObjectMetadataOrNull(KEY)).thenReturn(metadata(1024, "application/pdf"));
        when(resume.getPortfolioUrl()).thenReturn(BASE + "old.pdf");

        PortfolioUploadResponseDto res = service.completePortfolioUpload(RESUME_ID, KEY, MEMBER_ID);

        assertThat(res.getPortfolioUrl()).isEqualTo(BASE + KEY);
        var order = inOrder(s3Service, resume, resumeRepository);
        order.verify(s3Service).makePublicRead(KEY);
        order.verify(resume).updatePortfolio(BASE + KEY);
        order.verify(resumeRepository).save(resume);
        order.verify(s3Service).deleteFileByUrl(BASE + "old.pdf");
        verify(redisUtil).deleteRedisKey(TICKET);
    }

    @Test
    void 완료_재요청은_중복_등록이나_삭제_없이_성공() {
        when(resume.getPortfolioUrl()).thenReturn(BASE + KEY);

        PortfolioUploadResponseDto res = service.completePortfolioUpload(RESUME_ID, KEY, MEMBER_ID);

        assertThat(res.getPortfolioUrl()).isEqualTo(BASE + KEY);
        verify(resume, never()).updatePortfolio(any());
        verify(s3Service, never()).deleteFileByUrl(anyString());
    }

    @Test
    void 삭제_시_DB_반영_후_S3_객체_삭제() {
        when(resume.getPortfolioUrl()).thenReturn(BASE + "old.pdf");

        service.deletePortfolio(RESUME_ID, MEMBER_ID);

        var order = inOrder(resume, resumeRepository, s3Service);
        order.verify(resume).updatePortfolio(null);
        order.verify(resumeRepository).save(resume);
        order.verify(s3Service).deleteFileByUrl(BASE + "old.pdf");
    }
}
