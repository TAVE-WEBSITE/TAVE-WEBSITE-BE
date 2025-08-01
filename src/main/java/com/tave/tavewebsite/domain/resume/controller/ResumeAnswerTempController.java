package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.controller.message.PersonalInfoSuccessMessage;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeReqDto;
import com.tave.tavewebsite.domain.resume.dto.wrapper.ResumeTempWrapper;
import com.tave.tavewebsite.domain.resume.service.ResumeAnswerTempService;
import com.tave.tavewebsite.global.security.utils.SecurityUtils;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.tave.tavewebsite.domain.resume.controller.message.PersonalInfoSuccessMessage.TEMP_SAVE_SUCCESS;

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
        Long memberId = SecurityUtils.getCurrentMember().getId();
        tempService.tempSaveAnswers(resumeId, page, tempDto, memberId);
        return SuccessResponse.ok(TEMP_SAVE_SUCCESS.getMessage());
    }

    // 전체 임시 저장 객체 조회
    @GetMapping("/{resumeId}")
    public SuccessResponse<ResumeTempWrapper> getTempAnswers(@PathVariable(name = "resumeId") Long resumeId) {
        Long memberId = SecurityUtils.getCurrentMember().getId();
        ResumeTempWrapper response = tempService.getTempSavedAnswers(resumeId, memberId);
        return new SuccessResponse<>(response, PersonalInfoSuccessMessage.TEMP_LOAD_SUCCESS.getMessage());
    }
}
