package com.tave.tavewebsite.domain.resume.batch.controller;

import com.tave.tavewebsite.domain.apply.initial.setup.service.ApplyInitialSetupService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/config/recruitment")
public class RecruitmentEmailBatchController {

    private final ApplyInitialSetupService applyInitialSetupService;

    @GetMapping("/document/email")
    public SuccessResponse runDocumentEmailBatch(HttpServletRequest request) {
        applyInitialSetupService.changeDocumentAnnouncementFlag(true, request);
        return SuccessResponse.ok(RecruitmentBatchSuccessMessage.DOCUMENT_RESULT_BATCH_JOB_EXECUTE.getMessage());
    }

    @GetMapping("/document/email/cancel")
    public SuccessResponse cancelDocumentEmailBatch(HttpServletRequest request) {
        applyInitialSetupService.changeDocumentAnnouncementFlag(false, request);
        return SuccessResponse.ok(RecruitmentBatchSuccessMessage.DOCUMENT_RESULT_BATCH_JOB_CANCEL.getMessage());
    }

    @GetMapping("/last/email")
    public SuccessResponse runLastEmailBatch(HttpServletRequest request) {
        applyInitialSetupService.changeLastAnnouncementFlag(true, request);
        return SuccessResponse.ok(RecruitmentBatchSuccessMessage.LAST_RESULT_BATCH_JOB_EXECUTE.getMessage());
    }

    @GetMapping("/last/email/cancel")
    public SuccessResponse cancelLastEmailBatch(HttpServletRequest request) {
        applyInitialSetupService.changeLastAnnouncementFlag(false, request);
        return SuccessResponse.ok(RecruitmentBatchSuccessMessage.LAST_RESULT_BATCH_JOB_CANCEL.getMessage());
    }
}
