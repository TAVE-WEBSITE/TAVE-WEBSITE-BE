package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.response.DetailResumeQuestionResponse;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.service.PersonalInfoService;
import com.tave.tavewebsite.domain.resume.service.ResumeQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member/resumes")
public class ResumeQuestionController {
    private final ResumeQuestionService resumeQuestionService;
    private final PersonalInfoService personalInfoService;

    @GetMapping("/{resumeId}/questions")
    public List<DetailResumeQuestionResponse> getResumeQuestionsByPage(
            @PathVariable Long resumeId,
            @RequestParam int page
    ) {
        Resume resume = personalInfoService.getResumeEntityById(resumeId);
        return resumeQuestionService.getResumeQuestionPage(resume, page);
    }
}
