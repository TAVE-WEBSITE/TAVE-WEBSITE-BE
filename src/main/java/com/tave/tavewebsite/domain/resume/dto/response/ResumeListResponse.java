package com.tave.tavewebsite.domain.resume.dto.response;

import java.util.List;

public record ResumeListResponse(
        List<CommonResumeResponse> common,
        List<SpecificResumeResponseDto> specific
) {
    public static ResumeListResponse of(List<CommonResumeResponse> common, List<SpecificResumeResponseDto> specific) {
        return new ResumeListResponse(common, specific);
    }
}