package com.tave.tavewebsite.domain.session.dto.request;

import com.tave.tavewebsite.domain.session.entity.Session;

public record SessionRequestDto(

        String title,
        String description,
        String generation,
        boolean isPublic

) {
}
