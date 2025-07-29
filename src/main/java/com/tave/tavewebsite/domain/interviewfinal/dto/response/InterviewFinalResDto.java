package com.tave.tavewebsite.domain.interviewfinal.dto.response;

import com.tave.tavewebsite.domain.member.entity.Sex;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.global.common.FieldType;

import java.time.LocalDate;

public record InterviewFinalResDto(
        Long id,
        FieldType field,
        String username,
        Sex sex,
        String university,
        LocalDate interviewDate,
        EvaluationStatus status,
        Long memberId,
        Long resumeId
) {
}
