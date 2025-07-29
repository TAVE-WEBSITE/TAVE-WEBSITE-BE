package com.tave.tavewebsite.domain.interviewfinal.dto.response;

import org.springframework.data.domain.Page;

public record InterviewFinalEvaluateResDto(
        long totalRecruiter,
        long notCompletedRecruiter,
        long completedRecruiter,
        Page<InterviewFinalResDto> dtos
) {
    public static InterviewFinalEvaluateResDto fromInterviewFinalResDto(
            long totalRecruiter,
            long notCompletedRecruiter,
            long completedRecruiter,
            Page<InterviewFinalResDto> finalResDtos
    ) {
        return new InterviewFinalEvaluateResDto(
                totalRecruiter,
                notCompletedRecruiter,
                completedRecruiter,
                finalResDtos
        );
    }
}
