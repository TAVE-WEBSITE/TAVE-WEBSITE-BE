package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.request.ResumeReqDto;
import com.tave.tavewebsite.domain.resume.dto.response.DetailResumeQuestionResponse;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.service.PersonalInfoService;
import com.tave.tavewebsite.domain.resume.service.ResumeQuestionService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tave.tavewebsite.domain.resume.controller.PersonalInfoSuccessMessage.ANSWER_CREATE_SUCCESS;
import static com.tave.tavewebsite.domain.resume.controller.PersonalInfoSuccessMessage.QUESTION_READ_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member/resumes")
public class ResumeQuestionController {
    private final ResumeQuestionService resumeQuestionService;
    private final PersonalInfoService personalInfoService;


    @PostMapping("/{resumeId}")
    public SuccessResponse saveAnswers(@PathVariable(name = "resumeId") Long resumeId,
                                       @RequestBody ResumeReqDto reqDto) {

        resumeQuestionService.createResumeAnswer(resumeId, reqDto);

        return new SuccessResponse(ANSWER_CREATE_SUCCESS.getMessage());
    }

    @GetMapping("/{resumeId}/questions")
    public SuccessResponse<List<DetailResumeQuestionResponse>> getResumeQuestionsByPage(
            @PathVariable Long resumeId,
            @RequestParam int page
    ) {
        Resume resume = personalInfoService.getResumeEntityById(resumeId);
        List<DetailResumeQuestionResponse> response = resumeQuestionService.getResumeQuestionPage(resume, page);
        return new SuccessResponse<>(response, QUESTION_READ_SUCCESS.getMessage());
    }
}
