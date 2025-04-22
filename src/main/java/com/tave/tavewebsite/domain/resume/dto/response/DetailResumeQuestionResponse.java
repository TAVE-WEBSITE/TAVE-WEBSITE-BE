package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.resume.entity.ResumeQuestion;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.Builder;

@Builder
public record DetailResumeQuestionResponse(

        Long id,
        String question,
        String answer,
        FieldType fieldType,
        Integer ordered

) {
    public static DetailResumeQuestionResponse from(ResumeQuestion resumeQuestion) {
        return DetailResumeQuestionResponse.builder()
                .id(resumeQuestion.getId())
                .question(resumeQuestion.getQuestion())
                .answer(resumeQuestion.getAnswer())
                .fieldType(resumeQuestion.getFieldType())
                .ordered(resumeQuestion.getOrdered())
                .build();
    }

    public boolean isCommon(){
        return fieldType == FieldType.COMMON;
    }
}
