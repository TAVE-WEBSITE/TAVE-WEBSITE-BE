package com.tave.tavewebsite.domain.resume.dto.response;

import java.util.List;

public record ResumeResponse(
        Long resumeId,
        List<CommonResumeResponse> common,
        List<SpecificResumeResponseDto> specific
) {
    public static ResumeResponse of(Long resumeId,
                                    List<CommonResumeResponse> common,
                                    List<SpecificResumeResponseDto> specific) {
        return new ResumeResponse(resumeId, common, specific);
    }
}
