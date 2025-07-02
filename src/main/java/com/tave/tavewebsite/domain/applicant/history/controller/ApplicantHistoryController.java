package com.tave.tavewebsite.domain.applicant.history.controller;

import com.tave.tavewebsite.domain.applicant.history.dto.response.ApplicantHistoryResponseDto;
import com.tave.tavewebsite.domain.applicant.history.service.ApplicantHistoryService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ApplicantHistoryController {

    private final ApplicantHistoryService applicantHistoryService;

    @GetMapping("/member/applicant/history/{id}")
    public SuccessResponse<List<ApplicantHistoryResponseDto>> getApplicantHistory(@PathVariable("id") Long id) {
        return new SuccessResponse<>(applicantHistoryService.getApplicantHistory(id),
                ApplicantHistorySuccessMessage.READ_SUCCESS.getMessage());
    }
}
