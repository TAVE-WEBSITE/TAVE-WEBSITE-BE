package com.tave.tavewebsite.domain.resume.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialLinksResponseDto {
    private String githubUrl;
    private String blogUrl;
    private String portfolioUrl;
}
