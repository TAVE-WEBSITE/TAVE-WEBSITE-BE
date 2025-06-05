package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.SocialLinksResponseDto;
import com.tave.tavewebsite.domain.resume.service.SocialLinksService;
import com.tave.tavewebsite.global.s3.service.S3Service;
import com.tave.tavewebsite.global.success.SuccessResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@RestController
@RequestMapping("/v1/member/resume/{resumeId}/social-links")
public class SocialLinksController {

    private final SocialLinksService socialLinksService;
    private final S3Service s3Service;

    public SocialLinksController(SocialLinksService socialLinksService, S3Service s3Service) {
        this.socialLinksService = socialLinksService;
        this.s3Service = s3Service;
    }

    // 소셜 링크 등록
    @PostMapping
    public SuccessResponse registerSocialLinks(@PathVariable("resumeId") Long resumeId,
                                               @RequestBody SocialLinksRequestDto socialLinksRequestDto) {
        socialLinksService.createSocialLinks(resumeId, socialLinksRequestDto);
        return SuccessResponse.ok(SocialLinksSuccessMessage.CREATE_SUCCESS.getMessage());
    }

    // 소셜 링크 조회
    @GetMapping
    public SuccessResponse<SocialLinksResponseDto> getSocialLinks(@PathVariable("resumeId") Long resumeId) {
        return new SuccessResponse<>(socialLinksService.getSocialLinks(resumeId),
                SocialLinksSuccessMessage.READ_SUCCESS.getMessage());
    }

    // 소셜 링크 업데이트
    @PatchMapping
    public SuccessResponse updateSocialLinks(@PathVariable("resumeId") Long resumeId,
                                             @RequestBody SocialLinksRequestDto socialLinksRequestDto) {
        socialLinksService.updateSocialLinks(resumeId, socialLinksRequestDto);
        return SuccessResponse.ok(SocialLinksSuccessMessage.UPDATE_SUCCESS.getMessage());
    }

    @PostMapping("/portfolio")
    public SuccessResponse updatePortfolio(@PathVariable("resumeId") Long resumeId,
                                           @RequestParam("file") MultipartFile file) {
        URL portfolioUrl = s3Service.uploadFile(file);
        socialLinksService.updatePortfolio(resumeId, portfolioUrl.toString());
        return SuccessResponse.ok(SocialLinksSuccessMessage.UPLOAD_SUCCESS.getMessage());
    }

    @PostMapping("/temp")
    public SuccessResponse saveSocialLinksTemporarily(@PathVariable("resumeId") Long resumeId,
                                                      @RequestBody SocialLinksRequestDto dto) {
        socialLinksService.saveSocialLinksToRedis(resumeId, dto);
        return SuccessResponse.ok(SocialLinksSuccessMessage.TEMP_SAVE_SUCCESS.getMessage());
    }

    @GetMapping("/temp")
    public SuccessResponse<SocialLinksRequestDto> getSocialLinksTemp(@PathVariable("resumeId") Long resumeId) {
        SocialLinksRequestDto dto = socialLinksService.getSocialLinksFromRedis(resumeId);
        return new SuccessResponse<>(dto, SocialLinksSuccessMessage.TEMP_READ_SUCCESS.getMessage());
    }

    @PostMapping("/portfolio/temp")
    public SuccessResponse savePortfolioTemp(@PathVariable("resumeId") Long resumeId,
                                             @RequestParam("file") MultipartFile file) {
        URL portfolioUrl = s3Service.uploadFile(file);
        socialLinksService.savePortfolioToRedis(resumeId, portfolioUrl.toString());
        return SuccessResponse.ok(SocialLinksSuccessMessage.PORTFOLIO_TEMP_SAVE_SUCCESS.getMessage());
    }

    @GetMapping("/portfolio/temp")
    public SuccessResponse<String> getPortfolioTemp(@PathVariable("resumeId") Long resumeId) {
        String portfolioUrl = socialLinksService.getPortfolioFromRedis(resumeId);
        return new SuccessResponse<>(portfolioUrl, SocialLinksSuccessMessage.PORTFOLIO_TEMP_READ_SUCCESS.getMessage());
    }
}
