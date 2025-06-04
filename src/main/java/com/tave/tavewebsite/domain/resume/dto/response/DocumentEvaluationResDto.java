package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.resume.entity.ResumeEvaluation;

public record DocumentEvaluationResDto(
        String username,
        double score,
        String opinion
) {
    public static DocumentEvaluationResDto from(ResumeEvaluation resumeEvaluation) {
        return new DocumentEvaluationResDto(
                resumeEvaluation.getMember().getUsername(),
                resumeEvaluation.getEvaluationScore(),
                resumeEvaluation.getEvaluationOpinion()
        );
    }
}
