package com.tave.tavewebsite.domain.review.dto.request;

import com.tave.tavewebsite.global.common.FieldType;
import jakarta.validation.constraints.NotNull;

public record ReviewRequestDto(

        String nickname,
        String generation,
        String companyName,
        FieldType field,
        @NotNull(message = "필수로 입력하셔야합니다.")
        String content,
        boolean isPublic

) {
}
