package com.tave.tavewebsite.domain.resume.controller;

import static com.tave.tavewebsite.domain.resume.controller.PersonalInfoSuccessMessage.TEMP_SAVE_SUCCESS;

import com.tave.tavewebsite.domain.resume.dto.request.ResumeReqDto;
import com.tave.tavewebsite.domain.resume.dto.wrapper.ResumeTempWrapper;
import com.tave.tavewebsite.domain.resume.service.ResumeAnswerTempService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member/resume/temp-answer")
public class ResumeAnswerTempController {

    private final ResumeAnswerTempService tempService;

    // 임시 저장
    @PostMapping("/{resumeId}")
    public SuccessResponse saveTempAnswers(@PathVariable(name = "resumeId") Long resumeId,
                                           @RequestParam(name = "page") int page,
                                           @RequestBody ResumeReqDto tempDto) {
        tempService.tempSaveAnswers(resumeId, page, tempDto);
        return SuccessResponse.ok(TEMP_SAVE_SUCCESS.getMessage());
    }

    // 전체 임시 저장 객체 조회
    @GetMapping("/{resumeId}")
    public SuccessResponse<ResumeTempWrapper> getTempAnswers(@PathVariable(name = "resumeId") Long resumeId) {
        ResumeTempWrapper response = tempService.getTempSavedAnswers(resumeId);
        return new SuccessResponse<>(response, PersonalInfoSuccessMessage.TEMP_LOAD_SUCCESS.getMessage());
    }
}
