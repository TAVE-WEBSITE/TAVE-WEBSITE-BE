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
import org.springframework.web.bind.annotation.RestController;

import static com.tave.tavewebsite.domain.apply.initial.setup.controller.ApplyInitialSetupSuccessMessage.CHECK_APPLY_RECRUIT_END_DATE;
import static com.tave.tavewebsite.domain.apply.initial.setup.controller.ApplyInitialSetupSuccessMessage.CHECK_DOCUMENT_RECRUIT_START_DATE;

@RestController
@RequiredArgsConstructor
public class ApplyInitialSetupController {

    private final ApplyInitialSetupService applyInitialSetupService;

    @GetMapping("/v1/normal/apply/setting")
    public SuccessResponse<ApplyInitialSetupReadResponseDto> readApplyInitialSetup() {
        return new SuccessResponse<>(applyInitialSetupService.getInitialSetup(),
                ApplyInitialSetupSuccessMessage.APPLY_INITIAL_SETUP_READ_SUCCESS.getMessage());
    }

    @PostMapping("/v1/admin/apply/setting")
    public SuccessResponse saveApplyInitialSetup(@RequestBody @Valid ApplyInitialSetupRequestDto dto) {
        applyInitialSetupService.saveInitialSetup(dto);
        return SuccessResponse.ok(ApplyInitialSetupSuccessMessage.APPLY_INITIAL_SETUP_SAVE_SUCCESS.getMessage());
    }

    @PatchMapping("/v1/admin/apply/setting")
    public SuccessResponse updateApplyInitialSetup(@RequestBody @Valid ApplyInitialSetupRequestDto dto) {
        applyInitialSetupService.updateInitialSetup(dto);
        return SuccessResponse.ok(ApplyInitialSetupSuccessMessage.APPLY_INITIAL_SETUP_UPDATE_SUCCESS.getMessage());
    }

    @DeleteMapping("/v1/admin/apply/setting")
    public SuccessResponse deleteApplyInitialSetup() {
        applyInitialSetupService.deleteInitialSetup();
        return SuccessResponse.ok(ApplyInitialSetupSuccessMessage.APPLY_INITIAL_SETUP_DELETE_SUCCESS.getMessage());
    }

    @GetMapping("/v1/member/apply-recruit/expiration")
    public SuccessResponse<Boolean> checkApplyEndDateExpiration() {
        boolean result = applyInitialSetupService.checkRecruitExpiration();

        return new SuccessResponse<>(result, CHECK_APPLY_RECRUIT_END_DATE.getMessage());
    }

    @GetMapping("/v1/member/apply-recruit/started")
    public SuccessResponse<Boolean> checkApplyStartDate() {
        boolean result = applyInitialSetupService.checkRecruitStartDate();
        return new SuccessResponse<>(result, CHECK_DOCUMENT_RECRUIT_START_DATE.getMessage());
    }
}
