package com.tave.tavewebsite.domain.review.dto.response;

import com.tave.tavewebsite.global.common.FieldType;
import lombok.Builder;

@Builder
public record ReviewManagerResponseDto(
        Long id,
        String nickname,
        String generation,
        String companyName,
        FieldType field,
        String content,
        boolean isPublic
){
}
