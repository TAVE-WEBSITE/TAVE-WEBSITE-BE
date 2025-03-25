package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.SocialLinksResponseDto;
import com.tave.tavewebsite.domain.resume.service.SocialLinksService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/member/resume/{resumeId}/social-links")
public class SocialLinksController {

    private final SocialLinksService socialLinksService;

    public SocialLinksController(SocialLinksService socialLinksService) {
        this.socialLinksService = socialLinksService;
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

}
