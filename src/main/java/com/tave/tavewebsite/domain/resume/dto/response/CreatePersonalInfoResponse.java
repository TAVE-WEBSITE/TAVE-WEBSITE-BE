package com.tave.tavewebsite.domain.resume.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePersonalInfoResponse {
    private String message;
    private ResumeQuestionResponse resumeQuestions;
    private Long resumeId;

    public static CreatePersonalInfoResponse of(String message, ResumeQuestionResponse resumeQuestions, Long resumeId) {
        return new CreatePersonalInfoResponse(message, resumeQuestions, resumeId);
    }
}
