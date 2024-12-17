package com.tave.tavewebsite.domain.session.dto.response;

import com.tave.tavewebsite.domain.session.entity.Session;

public record SessionResponseDto(
        Long sessionId,
        String title,
        String description,
        String generation,
        boolean isPublic,
        String imgUrl

) {
    public static SessionResponseDto from(Session session) {
        return new SessionResponseDto(
                session.getId(),
                session.getTitle(),
                session.getDescription(),
                session.getGeneration(),
                session.isPublic(),
                session.getImgUrl()
        );
    }
}
