package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.controller.message.SocialLinksSuccessMessage;
import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.SocialLinksResponseDto;
import com.tave.tavewebsite.domain.resume.exception.FileSizeExceededException;
import com.tave.tavewebsite.domain.resume.service.SocialLinksService;
import com.tave.tavewebsite.global.s3.service.S3Service;
import com.tave.tavewebsite.global.success.SuccessResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@RestController
@RequestMapping("/v1/member/resume/{resumeId}")
public class SocialLinksController {

    private final SocialLinksService socialLinksService;
    private final S3Service s3Service;

    public SocialLinksController(SocialLinksService socialLinksService, S3Service s3Service) {
        this.socialLinksService = socialLinksService;
        this.s3Service = s3Service;
    }

    // 소셜 링크 등록
    @PostMapping("/social-links")
    public SuccessResponse registerSocialLinks(@PathVariable("resumeId") Long resumeId,
                                               @RequestBody SocialLinksRequestDto socialLinksRequestDto) {
        socialLinksService.createSocialLinks(resumeId, socialLinksRequestDto);
        socialLinksService.saveSocialLinksToRedis(resumeId, socialLinksRequestDto);
        return SuccessResponse.ok(SocialLinksSuccessMessage.CREATE_SUCCESS.getMessage());
    }

    // 소셜 링크 조회
    @GetMapping("/social-links")
    public SuccessResponse<SocialLinksResponseDto> getSocialLinks(@PathVariable("resumeId") Long resumeId) {
        return new SuccessResponse<>(socialLinksService.getSocialLinks(resumeId),
                SocialLinksSuccessMessage.READ_SUCCESS.getMessage());
    }

    // 소셜 링크 업데이트
    @PatchMapping("/social-links")
    public SuccessResponse updateSocialLinks(@PathVariable("resumeId") Long resumeId,
                                             @RequestBody SocialLinksRequestDto socialLinksRequestDto) {
        socialLinksService.updateSocialLinks(resumeId, socialLinksRequestDto);
        socialLinksService.saveSocialLinksToRedis(resumeId, socialLinksRequestDto);
        return SuccessResponse.ok(SocialLinksSuccessMessage.UPDATE_SUCCESS.getMessage());
    }

    // 포트폴리오 업로드
    @PostMapping("/portfolio")
    public SuccessResponse updatePortfolio(@PathVariable("resumeId") Long resumeId,
                                           @RequestParam("file") MultipartFile file) {
        final long MAX_FILE_SIZE = 300 * 1024 * 1024; // 300MB

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileSizeExceededException();
        }

        URL portfolioUrl = s3Service.uploadFile(file);
        socialLinksService.updatePortfolio(resumeId, portfolioUrl.toString());
        socialLinksService.savePortfolioToRedis(resumeId, portfolioUrl.toString());
        return SuccessResponse.ok(SocialLinksSuccessMessage.UPLOAD_SUCCESS.getMessage());
    }

}
