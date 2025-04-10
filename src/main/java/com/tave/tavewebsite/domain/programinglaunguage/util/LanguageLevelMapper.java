package com.tave.tavewebsite.domain.programinglaunguage.util;

import com.tave.tavewebsite.domain.programinglaunguage.dto.response.LanguageLevelResponseDto;
import com.tave.tavewebsite.domain.programinglaunguage.entity.LanguageLevel;

public class LanguageLevelMapper {

    public static LanguageLevelResponseDto toLanguageLevelResponseDto(LanguageLevel languageLevel) {
        return new LanguageLevelResponseDto(languageLevel.getLanguage(), languageLevel.getLevel());
    }
}
