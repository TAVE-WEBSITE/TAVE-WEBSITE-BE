package com.tave.tavewebsite.domain.interviewfinal.dto.response;

import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;

public record InterviewFinalResDto(
        Long id,
        String field,
        String username,
        String sex,
        String university,
        String interviewDate,
        EvaluationStatus status,
        Long memberId,
        Long resumeId
) {
}
