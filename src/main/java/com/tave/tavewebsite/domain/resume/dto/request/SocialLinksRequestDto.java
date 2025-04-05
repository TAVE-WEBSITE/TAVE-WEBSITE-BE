package com.tave.tavewebsite.domain.resume.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLinksRequestDto {
    private String blogUrl;
    private String githubUrl;
}
