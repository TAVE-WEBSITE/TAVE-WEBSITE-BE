package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoRequestDto;
import com.tave.tavewebsite.domain.resume.dto.request.TempPersonalInfoDto;
import com.tave.tavewebsite.domain.resume.dto.response.CreatePersonalInfoResponse;
import com.tave.tavewebsite.domain.resume.dto.response.PersonalInfoResponseDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeQuestionResponse;
import com.tave.tavewebsite.domain.resume.service.PersonalInfoService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.tave.tavewebsite.domain.resume.controller.PersonalInfoSuccessMessage.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1/member/info")
public class PersonalInfoController {

    private final PersonalInfoService personalInfoService;

    // 개인정보 저장 및 질문 목록 반환
    @PostMapping("/{memberId}")
    public SuccessResponse<CreatePersonalInfoResponse> createPersonalInfoWithQuestions(@PathVariable("memberId") Long memberId,
                                                                                       @RequestBody @Valid PersonalInfoRequestDto requestDto) {
        ResumeQuestionResponse questions = personalInfoService.createPersonalInfo(memberId, requestDto);

        CreatePersonalInfoResponse response = CreatePersonalInfoResponse.of(
                PersonalInfoSuccessMessage.CREATE_SUCCESS.getMessage(), questions);

        return new SuccessResponse<>(response, PersonalInfoSuccessMessage.CREATE_SUCCESS.getMessage());
    }

    // 개인정보 조회
    @GetMapping("/{resumeId}")
    public SuccessResponse<PersonalInfoResponseDto> getPersonalInfo(@PathVariable("resumeId") Long resumeId) {
        return new SuccessResponse<>(personalInfoService.getPersonalInfo(resumeId),
                READ_SUCCESS.getMessage());
    }

    // 개인정보 수정
    @PatchMapping("/{resumeId}")
    public SuccessResponse updatePersonalInfo(@PathVariable("resumeId") Long resumeId,
                                              @RequestBody @Valid PersonalInfoRequestDto requestDto) {
        personalInfoService.updatePersonalInfo(resumeId, requestDto);
        return SuccessResponse.ok(UPDATE_SUCCESS.getMessage());
    }

    // 개인정보 삭제
    @DeleteMapping("/{resumeId}")
    public SuccessResponse deletePersonalInfo(@PathVariable("resumeId") Long resumeId) {
        personalInfoService.deletePersonalInfo(resumeId);
        return SuccessResponse.ok(DELETE_SUCCESS.getMessage());
    }

    // 개인정보 임시 저장
    @PostMapping("/temp-save/{memberId}")
    public SuccessResponse tempSavePersonalInfo(@PathVariable("memberId") Long memberId,
                                                @RequestBody @Valid PersonalInfoRequestDto requestDto) {
        personalInfoService.tempSavePersonalInfo(memberId, requestDto);
        return SuccessResponse.ok(TEMP_SAVE_SUCCESS.getMessage());
    }

    // 임시 저장 개인정보 불러오기
    @GetMapping("/temp-save/{memberId}")
    public SuccessResponse<TempPersonalInfoDto> getTempSavedPersonalInfo(@PathVariable("memberId") Long memberId) {
        TempPersonalInfoDto response = personalInfoService.getTempSavedPersonalInfo(memberId);
        return new SuccessResponse<>(response, TEMP_LOAD_SUCCESS.getMessage());
    }

}