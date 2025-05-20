package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.request.ResumeAnswerTempDto;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeAnswerTempWrapper;
import com.tave.tavewebsite.domain.resume.service.ResumeAnswerTempService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tave.tavewebsite.domain.resume.controller.PersonalInfoSuccessMessage.TEMP_LOAD_SUCCESS;
import static com.tave.tavewebsite.domain.resume.controller.PersonalInfoSuccessMessage.TEMP_SAVE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member/resume/temp-answer")
public class ResumeAnswerTempController {

    private final ResumeAnswerTempService tempService;

    // 임시 저장
    @PostMapping("/{resumeId}")
    public SuccessResponse saveTempAnswers(@PathVariable Long resumeId,
                                           @RequestParam int page,
                                           @RequestBody List<ResumeAnswerTempDto> answers) {
        tempService.tempSaveAnswers(resumeId, page, answers);
        return SuccessResponse.ok(TEMP_SAVE_SUCCESS.getMessage());
    }

    // 임시 저장 불러오기
    @GetMapping("/{resumeId}")
    public SuccessResponse<ResumeAnswerTempWrapper> getTempAnswers(@PathVariable Long resumeId) {
        ResumeAnswerTempWrapper response = tempService.getTempSavedAnswers(resumeId);
        return new SuccessResponse<>(response, TEMP_LOAD_SUCCESS.getMessage());
    }

}
