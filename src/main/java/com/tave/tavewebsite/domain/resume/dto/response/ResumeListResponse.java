package com.tave.tavewebsite.domain.resume.dto.response;

import java.util.List;

public record ResumeListResponse(
        List<ResumeResponse> resumeList
) {
    public static ResumeListResponse of(List<ResumeResponse> resumeList) {
        return new ResumeListResponse(resumeList);
    }
}
