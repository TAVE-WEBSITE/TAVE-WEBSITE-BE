package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.request.DocumentEvaluationReqDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeEvaluateResDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.domain.resume.service.ResumeEvaluateService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.tave.tavewebsite.domain.resume.controller.ResumeEvaluateSuccessMessage.CREATE_SUCCESS;
import static com.tave.tavewebsite.domain.resume.controller.ResumeEvaluateSuccessMessage.READ_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/manager/resume/evaluate")
public class ResumeEvaluateController {

    private final ResumeEvaluateService resumeEvaluateService;

    @PostMapping("/{resumeId}")
    public SuccessResponse createDocumentEvaluation(@PathVariable Long resumeId,
                                                    @RequestBody @Valid DocumentEvaluationReqDto reqDto) {

        resumeEvaluateService.createDocumentEvaluation(resumeId, reqDto);

        return new SuccessResponse(CREATE_SUCCESS.getMessage());
    }

    @GetMapping
    public SuccessResponse<ResumeEvaluateResDto> getEvaluationList(
            @RequestParam EvaluationStatus status,
            @PageableDefault(size = 7) Pageable pageable
    ) {

        return new SuccessResponse<>(
                resumeEvaluateService.getResumes(status, pageable),
                READ_SUCCESS.getMessage());
    }
}
