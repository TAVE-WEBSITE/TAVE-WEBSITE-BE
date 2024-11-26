package com.tave.tavewebsite.domain.review.dto.response;


import com.tave.tavewebsite.global.common.FieldType;
import lombok.Builder;

@Builder
public record ReviewResponseDto(
        Long id,
        String nickname,
        String generation,
        FieldType field,
        String content,
        boolean isPublic
) {
}
