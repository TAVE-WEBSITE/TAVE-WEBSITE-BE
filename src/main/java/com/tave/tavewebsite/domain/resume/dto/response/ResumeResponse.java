package com.tave.tavewebsite.domain.resume.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ResumeResponse(
        Long resumeId,
        ResumeMemberInfoDto resumeMemberInfoDto,
        List<CommonResumeResponse> common,
        List<SpecificResumeResponseDto> specific
) {
    public static ResumeResponse of(Long resumeId,
                                    List<CommonResumeResponse> common,
                                    List<SpecificResumeResponseDto> specific,
                                    ResumeMemberInfoDto resumeMemberInfoDto
                                    )
    {
        return ResumeResponse.builder()
                .resumeId(resumeId)
                .resumeMemberInfoDto(resumeMemberInfoDto)
                .common(common)
                .specific(specific)
                .build();
    }
}
