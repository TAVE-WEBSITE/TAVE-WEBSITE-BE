package com.tave.tavewebsite.domain.resume.dto.response;

import org.springframework.data.domain.Page;

public record ResumeEvaluateResDto(
        int totalRecruiter,
        int notCompletedRecruiter,
        int completedRecruiter,
        Page<ResumeResDto> resumeResDtos
) {
    public static ResumeEvaluateResDto fromResume(int totalRecruiter,
                                                  int notCompletedRecruiter,
                                                  int completedRecruiter,
                                                  Page<ResumeResDto> resumeResDtos) {
        return new ResumeEvaluateResDto(
                totalRecruiter,
                notCompletedRecruiter,
                completedRecruiter,
                resumeResDtos
        );
    }
}
