package com.tave.tavewebsite.domain.programinglaunguage.dto.request;

import com.tave.tavewebsite.global.common.FieldType;

public record ProgramingLanguageRequestDto(
        FieldType field,
        String language
) {
}
