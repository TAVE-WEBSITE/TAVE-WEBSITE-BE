package com.tave.tavewebsite.domain.finalpass.controller;

import com.tave.tavewebsite.domain.finalpass.dto.request.FinalPassRequestDto;
import com.tave.tavewebsite.domain.finalpass.dto.response.FinalPassResponseDto;
import com.tave.tavewebsite.domain.finalpass.service.FinalPassService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FinalPassController {
    private final FinalPassService finalPassService;

    @PostMapping("/v1/admin/final-pass")
    public SuccessResponse<FinalPassResponseDto> createFinalPass(@RequestBody FinalPassRequestDto requestDto) {
        FinalPassResponseDto dto = finalPassService.createFinalPass(requestDto);
        return new SuccessResponse<>(dto, FinalPassSuccessMessage.CREATE_FINAL_PASS.getMessage());
    }

    @GetMapping("/v1/admin/final-pass")
    public SuccessResponse<FinalPassResponseDto> getFinalPass() {
        FinalPassResponseDto dto = finalPassService.getFinalPass();
        return new SuccessResponse<>(dto, FinalPassSuccessMessage.READ_FINAL_PASS.getMessage());
    }

    @GetMapping("/v1/member/final-pass")
    public SuccessResponse<FinalPassResponseDto> getFinalPassForMember() {
        FinalPassResponseDto dto = finalPassService.getFinalPass();
        return new SuccessResponse<>(dto, FinalPassSuccessMessage.READ_FINAL_PASS.getMessage());
    }
}
