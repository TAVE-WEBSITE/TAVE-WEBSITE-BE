package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.resume.entity.Resume;

import java.util.List;

public record CommonResumeResponse(
        Long resumeId,
        String blogUrl,
        String githubUrl,
        String portfolioUrl,
        List<DetailResumeQuestionResponse> commonQuestions
) {
    public static CommonResumeResponse of(Resume resume,
                                          List<DetailResumeQuestionResponse> commonQuestions) {
        return new CommonResumeResponse(
                resume.getId(),
                resume.getBlogUrl(),
                resume.getGithubUrl(),
                resume.getPortfolioUrl(),
                commonQuestions
        );
    }
}
