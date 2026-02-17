package com.tave.tavewebsite.domain.resume.dto.response;

import org.springframework.data.domain.Page;

public record ResumeEvaluateResDto(
        long totalRecruiter,
        long notCompletedRecruiter,
        long completedRecruiter,
        Page<ResumeResDto> resumeResDtos,
        String generation
) {
    public static ResumeEvaluateResDto fromResume(
            long totalRecruiter,
            long notCompletedRecruiter,
            long completedRecruiter,
            Page<ResumeResDto> resumeResDtos,
            String generation
                                                  ) {
        return new ResumeEvaluateResDto(
                totalRecruiter,
                notCompletedRecruiter,
                completedRecruiter,
                resumeResDtos,
                generation
        );
    }
}
