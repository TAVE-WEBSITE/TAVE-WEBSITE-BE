package com.tave.tavewebsite.domain.question.dto.request;

import com.tave.tavewebsite.domain.resume.entity.AnswerType;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record QuestionSaveRequest(
        @NotBlank(message = "질문 내용을 입력해주세요.") String content,
        @NotNull FieldType fieldType,
        @NotNull Integer textLength,
        AnswerType answerType,
        @NotNull Boolean required

        // todo AnswerType 에 대해서 DTO 로 받아 처리해야할 듯.

) {
}
