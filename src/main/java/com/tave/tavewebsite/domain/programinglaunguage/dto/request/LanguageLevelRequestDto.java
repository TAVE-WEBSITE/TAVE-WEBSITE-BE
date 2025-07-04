package com.tave.tavewebsite.domain.programinglaunguage.dto.request;

import com.tave.tavewebsite.domain.programinglaunguage.entity.LanguageLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record LanguageLevelRequestDto(
        String language,
        @Max(value = 5, message = "1 이상, 5 이하의 숫자만 가능합니다.")
        @Min(value = 1, message = "1 이상, 5 이하의 숫자만 가능합니다.")
        Integer level
) {
    public static LanguageLevelRequestDto fromEntity(LanguageLevel entity) {
        return new LanguageLevelRequestDto(
                entity.getLanguage(),
                entity.getLevel()
        );
    }
}
