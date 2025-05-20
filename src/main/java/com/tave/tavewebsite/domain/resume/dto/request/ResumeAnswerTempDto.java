package com.tave.tavewebsite.domain.resume.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnswerTempDto {
    private Long resumeQuestionId;
    private String answer;
}