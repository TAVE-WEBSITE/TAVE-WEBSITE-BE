package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.request.DocumentEvaluationReqDto;
import com.tave.tavewebsite.domain.resume.dto.request.FinalDocumentEvaluationReqDto;
import com.tave.tavewebsite.domain.resume.dto.response.DocumentEvaluationResDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeEvaluateResDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.domain.resume.service.ResumeEvaluateService;
import com.tave.tavewebsite.global.common.FieldType;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tave.tavewebsite.domain.resume.controller.message.ResumeEvaluateSuccessMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/manager/resume/evaluate")
public class ResumeEvaluateController {

    private final ResumeEvaluateService resumeEvaluateService;

    // 서류 평가하기
    @PostMapping("/{resumeId}")
    public SuccessResponse createDocumentEvaluation(@PathVariable Long resumeId,
                                                    @RequestBody @Valid DocumentEvaluationReqDto reqDto) {

        resumeEvaluateService.createDocumentEvaluation(resumeId, reqDto);

        return new SuccessResponse(CREATE_SUCCESS.getMessage());
    }

    // 서류 리스트 보기
    @GetMapping
    public SuccessResponse<ResumeEvaluateResDto> getDocumentEvaluationList(
            @RequestParam(required = false) EvaluationStatus status,
            @RequestParam(required = false) FieldType type,
            @PageableDefault(size = 7) Pageable pageable
    ) {

        return new SuccessResponse<>(
                resumeEvaluateService.getDocumentResumes(status, type, pageable),
                READ_SUCCESS.getMessage());
    }

    // 최종 서류 평가 리스트 보기
    @GetMapping("/final")
    public SuccessResponse<ResumeEvaluateResDto> getFinalDocumentEvaluationList(
            @RequestParam(required = false) EvaluationStatus status,
            @RequestParam(required = false) FieldType type,
            @PageableDefault(size = 7) Pageable pageable
    ) {

        return new SuccessResponse<>(
                resumeEvaluateService.getFinalDocumentResumes(status, type, pageable),
                READ_SUCCESS.getMessage());
    }

    @PostMapping("/final/{resumeId}")
    public SuccessResponse createFinalDocumentEvaluation(
            @PathVariable(name = "resumeId") Long resumeId,
            @RequestBody FinalDocumentEvaluationReqDto reqDto
            ) {

        resumeEvaluateService.createFinalDocumentEvaluation(resumeId, reqDto);
        return new SuccessResponse(CREATE_FINAL_STATUS_SUCCESS.getMessage());
    }

    // 기존 평가 작성 한 인원들 보기
    @GetMapping("/final/{resumeId}")
    public SuccessResponse<List<DocumentEvaluationResDto>> getDetailedEvaluation(
            @PathVariable(name = "resumeId") Long resumeId) {

        return new SuccessResponse<>(
                resumeEvaluateService.getDocumentEvaluations(resumeId),
                READ_SUCCESS.getMessage()
        );
    }
}
