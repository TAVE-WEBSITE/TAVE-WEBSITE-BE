package com.tave.tavewebsite.domain.resume.dto.request;

import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;

public record FinalDocumentEvaluationReqDto(
        EvaluationStatus status
) {
}
