package com.tave.tavewebsite.domain.question.dto.response;

import com.tave.tavewebsite.domain.question.entity.Question;
import com.tave.tavewebsite.domain.resume.entity.AnswerType;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.Builder;

@Builder
public record QuestionDetailsResponse(
        Long id,
        String content,
        FieldType fieldType,
        Integer ordered,
        Integer textLength,
        AnswerType answerType
) {
    public static QuestionDetailsResponse of(Question question) {
        return QuestionDetailsResponse.builder()
                .id(question.getId())
                .content(question.getContent())
                .fieldType(question.getFieldType())
                .ordered(question.getOrdered())
                .textLength(question.getTextLength())
                .answerType(question.getAnswerType())
                .build();
    }
}
