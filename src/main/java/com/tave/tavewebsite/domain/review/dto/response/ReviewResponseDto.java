package com.tave.tavewebsite.domain.review.dto.response;


import com.tave.tavewebsite.global.common.FieldType;
import lombok.Builder;

@Builder
public record ReviewResponseDto(
        String nickname,
        String generation,
        String companyName,
        String field,
        String content,
        boolean isPublic
) {
}
