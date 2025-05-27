package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.resume.entity.Resume;

import java.util.List;

public record ResumeEvaluateResDto(
        int totalRecruiter,
        int notCompletedRecruiter,
        int completedRecruiter,
        List<ResumeResDto> resumeResDtos
) {
    public static ResumeEvaluateResDto fromResume(int totalRecruiter,
                                                  int notCompletedRecruiter,
                                                  int completedRecruiter,
                                                  List<ResumeResDto> resumeResDtos) {
        return new ResumeEvaluateResDto(
                totalRecruiter,
                notCompletedRecruiter,
                completedRecruiter,
                resumeResDtos
        );
    }
}
