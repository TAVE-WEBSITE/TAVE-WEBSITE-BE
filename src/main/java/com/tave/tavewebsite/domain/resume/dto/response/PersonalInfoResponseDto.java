package com.tave.tavewebsite.domain.resume.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PersonalInfoResponseDto {
    private final String school;
    private final String major;
    private final String minor;
    private final String field;
}
