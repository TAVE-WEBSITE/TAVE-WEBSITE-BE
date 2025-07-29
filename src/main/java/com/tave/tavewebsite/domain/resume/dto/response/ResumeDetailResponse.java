package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.programinglaunguage.dto.response.LanguageLevelResponseDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;

import java.util.List;

public record ResumeDetailResponse(
        Long resumeId,
        CommonResumeResponse common,
        SpecificResumeResponseDto specific,
        ResumeMemberInfoDto member
) {
    public static ResumeDetailResponse of(Resume resume,
                                          List<DetailResumeQuestionResponse> commonQuestions,
                                          List<DetailResumeQuestionResponse> specificQuestions,
                                          List<LanguageLevelResponseDto> languageLevels) {
        return new ResumeDetailResponse(
                resume.getId(),
                CommonResumeResponse.of(resume, commonQuestions),
                SpecificResumeResponseDto.of(resume.getId(), specificQuestions, languageLevels),
                ResumeMemberInfoDto.of(resume)
        );
    }
}
