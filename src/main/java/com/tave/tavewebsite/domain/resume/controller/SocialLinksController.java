package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.controller.message.SocialLinksSuccessMessage;
import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.PortfolioUploadResponseDto;
import com.tave.tavewebsite.domain.resume.dto.response.SocialLinksResponseDto;
import com.tave.tavewebsite.domain.resume.service.SocialLinksService;
import com.tave.tavewebsite.domain.resume.validator.FileValidator;
import com.tave.tavewebsite.global.s3.service.S3Service;
import com.tave.tavewebsite.global.security.utils.SecurityUtils;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member/resume/{resumeId}")
public class SocialLinksController {

    private final SocialLinksService socialLinksService;
    private final S3Service s3Service;
    private final FileValidator fileValidator;

    // 소셜 링크 등록
    @PostMapping("/social-links")
    public SuccessResponse registerSocialLinks(@PathVariable("resumeId") Long resumeId,
                                               @RequestBody SocialLinksRequestDto socialLinksRequestDto) {
        Long memberId = SecurityUtils.getCurrentMember().getId();
        socialLinksService.createSocialLinks(resumeId, socialLinksRequestDto, memberId);
        socialLinksService.saveSocialLinksToRedis(resumeId, socialLinksRequestDto, memberId);
        return SuccessResponse.ok(SocialLinksSuccessMessage.CREATE_SUCCESS.getMessage());
    }

    // 소셜 링크 조회
    @GetMapping("/social-links")
    public SuccessResponse<SocialLinksResponseDto> getSocialLinks(@PathVariable("resumeId") Long resumeId) {
        Long memberId = SecurityUtils.getCurrentMember().getId();
        SocialLinksResponseDto dto = socialLinksService.getSocialLinks(resumeId, memberId);
        return new SuccessResponse<>(dto, SocialLinksSuccessMessage.READ_SUCCESS.getMessage());
    }

    // 소셜 링크 업데이트
    @PatchMapping("/social-links")
    public SuccessResponse updateSocialLinks(@PathVariable("resumeId") Long resumeId,
                                             @RequestBody SocialLinksRequestDto socialLinksRequestDto) {
        Long memberId = SecurityUtils.getCurrentMember().getId();
        socialLinksService.updateSocialLinks(resumeId, socialLinksRequestDto, memberId);
        socialLinksService.saveSocialLinksToRedis(resumeId, socialLinksRequestDto, memberId);
        return SuccessResponse.ok(SocialLinksSuccessMessage.UPDATE_SUCCESS.getMessage());
    }

    // 포트폴리오 업로드
    @PostMapping("/portfolio")
    public SuccessResponse updatePortfolio(@PathVariable("resumeId") Long resumeId,
                                           @RequestParam("file") MultipartFile file) {
        Long memberId = SecurityUtils.getCurrentMember().getId();

        fileValidator.validateSize(file);
        URL portfolioUrl = s3Service.uploadFile(file);

        socialLinksService.updatePortfolio(resumeId, portfolioUrl.toString(), memberId);
        socialLinksService.savePortfolioToRedis(resumeId, portfolioUrl.toString(), memberId);
        PortfolioUploadResponseDto responseDto = new PortfolioUploadResponseDto(portfolioUrl.toString());
        return new SuccessResponse<>(responseDto, SocialLinksSuccessMessage.UPLOAD_SUCCESS.getMessage());
    }

    @GetMapping("/social-links/detail")
    public SuccessResponse<SocialLinksResponseDto> getSocialLinksDetail(@PathVariable("resumeId") Long resumeId) {
        Long memberId = SecurityUtils.getCurrentMember().getId();
        SocialLinksResponseDto dto = socialLinksService.getSocialLinks(resumeId, memberId);
        return new SuccessResponse<>(dto, SocialLinksSuccessMessage.READ_SUCCESS.getMessage());
    }

}
