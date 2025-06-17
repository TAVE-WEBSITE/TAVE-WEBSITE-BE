package com.tave.tavewebsite.domain.resume.batch.controller;

import com.tave.tavewebsite.domain.apply.initial.setup.service.ApplyInitialSetupService;
import com.tave.tavewebsite.domain.resume.batch.service.RecruitmentEmailBatchService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/config/recruitment")
public class RecruitmentEmailBatchController {

    private final RecruitmentEmailBatchService recruitmentEmailBatchService;
    private final ApplyInitialSetupService applyInitialSetupService;

    @GetMapping("/document/email")
    public SuccessResponse runRecruitmentEmailBatch() {
        applyInitialSetupService.changeDocumentAnnouncementFlag(true);
        return SuccessResponse.ok(RecruitmentBatchSuccessMessage.DOCUMENT_RESULT_BATCH_JOB_EXECUTE.getMessage());
    }
}
