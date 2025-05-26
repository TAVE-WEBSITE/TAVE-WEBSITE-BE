package com.tave.tavewebsite.domain.apply.initial.setup.controller;

import com.tave.tavewebsite.domain.apply.initial.setup.dto.request.ApplyInitialSetupRequestDto;
import com.tave.tavewebsite.domain.apply.initial.setup.dto.response.ApplyInitialSetupReadResponseDto;
import com.tave.tavewebsite.domain.apply.initial.setup.service.ApplyInitialSetupService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin/apply/setting")
@RequiredArgsConstructor
public class ApplyInitialSetupController {

    private final ApplyInitialSetupService applyInitialSetupService;

    @GetMapping
    public SuccessResponse<ApplyInitialSetupReadResponseDto> readApplyInitialSetup() {
        return new SuccessResponse<>(applyInitialSetupService.getInitialSetup(),
                ApplyInitialSetupSuccessMessage.APPLY_INITIAL_SETUP_READ_SUCCESS.getMessage());
    }

    @PostMapping
    public SuccessResponse saveApplyInitialSetup(@RequestBody @Valid ApplyInitialSetupRequestDto dto) {
        applyInitialSetupService.saveInitialSetup(dto);
        return SuccessResponse.ok(ApplyInitialSetupSuccessMessage.APPLY_INITIAL_SETUP_SAVE_SUCCESS.getMessage());
    }

    @PatchMapping
    public SuccessResponse updateApplyInitialSetup(@RequestBody @Valid ApplyInitialSetupRequestDto dto) {
        applyInitialSetupService.updateInitialSetup(dto);
        return SuccessResponse.ok(ApplyInitialSetupSuccessMessage.APPLY_INITIAL_SETUP_UPDATE_SUCCESS.getMessage());
    }

    @DeleteMapping
    public SuccessResponse deleteApplyInitialSetup() {
        applyInitialSetupService.deleteInitialSetup();
        return SuccessResponse.ok(ApplyInitialSetupSuccessMessage.APPLY_INITIAL_SETUP_DELETE_SUCCESS.getMessage());
    }
}
