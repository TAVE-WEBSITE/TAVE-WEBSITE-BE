package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.resume.entity.Resume;
import lombok.Builder;

@Builder
public record ResumePortfolioDto(
        String portfolioUrl,
        String memberName
) {
    public static ResumePortfolioDto of(Resume resume) {
        return ResumePortfolioDto.builder()
                .portfolioUrl(resume.getPortfolioUrl())
                .memberName(resume.getMember().getUsername())
                .build();
    }
}
