package com.tave.tavewebsite.domain.programinglaunguage.dto.response;

import com.tave.tavewebsite.domain.programinglaunguage.entity.LanguageLevel;

public record LanguageLevelResponseDto(
        String language,
        Integer level
) {
    public static LanguageLevelResponseDto fromEntity(LanguageLevel entity) {
        return new LanguageLevelResponseDto(entity.getLanguage(), entity.getLevel());
    }
}
