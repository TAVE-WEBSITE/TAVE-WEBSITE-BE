package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoRequestDto;
import com.tave.tavewebsite.domain.resume.service.PersonalInfoService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1/normal/info")
public class PersonalInfoController {

    private final PersonalInfoService personalInfoService;

    // 개인정보 저장 (새로운 지원서 생성)
    @PostMapping("/{memberId}")
    public SuccessResponse createPersonalInfo(@PathVariable("memberId") Long memberId,
                                              @RequestBody @Valid PersonalInfoRequestDto requestDto) {
        personalInfoService.createPersonalInfo(memberId, requestDto);
        return SuccessResponse.ok(PersonalInfoSuccessMessage.CREATE_SUCCESS.getMessage());
    }
}