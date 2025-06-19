package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.service.ResumeEmailService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member/resume/email")
public class ResumeEmailController {

    private final ResumeEmailService resumeEmailService;

    @GetMapping("/{resumeId}")
    public SuccessResponse sendResumeEmail(@PathVariable("resumeId") Long resumeId) {
        resumeEmailService.sendResumeEmail(resumeId);
        return SuccessResponse.ok(PersonalInfoSuccessMessage.RESUME_EMAIL_SEND_SUCCESS.getMessage());
    }

}
