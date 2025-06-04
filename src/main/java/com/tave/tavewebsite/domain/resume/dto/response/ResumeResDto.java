package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.member.entity.Sex;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.global.common.FieldType;

import java.time.LocalDateTime;

public record ResumeResDto(
        Long id,
        FieldType fieldType,
        String name,
        Sex sex,
        String school,
        LocalDateTime recruitTime,
        Long count,
        EvaluationStatus status
) {
}
