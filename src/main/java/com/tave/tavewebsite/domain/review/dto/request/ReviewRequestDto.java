package com.tave.tavewebsite.domain.review.dto.request;

import com.tave.tavewebsite.global.common.FieldType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReviewRequestDto(

        String nickname,
        @Pattern(regexp = "^[0-9]+$", message = "기수는 숫자만 입력 가능합니다.")
        String generation,
        String companyName,
        FieldType field,
        @NotNull(message = "필수로 입력하셔야합니다.")
        String content,
        boolean isPublic

) {
}
