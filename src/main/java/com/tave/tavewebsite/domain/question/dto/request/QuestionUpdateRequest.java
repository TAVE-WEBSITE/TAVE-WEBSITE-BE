package com.tave.tavewebsite.domain.question.dto.request;

import com.tave.tavewebsite.global.common.FieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionUpdateRequest(
        @NotNull Long id,
        @NotBlank(message = "질문 수정 내용을 입력해주세요.") String content,
        @NotNull(message = "질문 분야를 입력해주세요") FieldType fieldType,
        @NotNull(message = "질문 우선순위를 입력해주세요.") Integer ordered,
        @NotNull(message = "제한 글자수를 입력해주세요") Integer textLength
) {
}
