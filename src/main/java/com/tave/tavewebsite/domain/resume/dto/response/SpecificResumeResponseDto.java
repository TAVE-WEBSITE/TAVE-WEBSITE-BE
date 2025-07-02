package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.programinglaunguage.dto.response.LanguageLevelResponseDto;

import java.util.List;

public record SpecificResumeResponseDto(
        Long resumeId,
        List<DetailResumeQuestionResponse> specificQuestions,
        List<LanguageLevelResponseDto> languageLevels
) {
    public static SpecificResumeResponseDto of(Long resumeId,
                                               List<DetailResumeQuestionResponse> specificQuestions,
                                               List<LanguageLevelResponseDto> languageLevels) {
        return new SpecificResumeResponseDto(resumeId, specificQuestions, languageLevels);
    }
}
