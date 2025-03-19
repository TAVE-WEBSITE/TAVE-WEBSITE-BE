package com.tave.tavewebsite.domain.programinglaunguage.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record LanguageLevelRequestDto(
        String language,
        @Max(value = 5, message = "1 이상, 5 이하의 숫자만 가능합니다.")
        @Min(value = 1, message = "1 이상, 5 이하의 숫자만 가능합니다.")
        Integer level
) {
}
