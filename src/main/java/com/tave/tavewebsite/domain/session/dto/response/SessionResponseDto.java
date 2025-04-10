package com.tave.tavewebsite.domain.session.dto.response;

import com.tave.tavewebsite.domain.session.entity.Session;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SessionResponseDto(
        Long sessionId,
        String title,
        String description,
        LocalDate eventDay,
        String imgUrl
) {
    public static SessionResponseDto from(Session session) {
        return SessionResponseDto.builder()
                .sessionId(session.getId())
                .title(session.getTitle())
                .description(session.getDescription())
                .eventDay(session.getEventDay())
                .imgUrl(session.getImgUrl())
                .build();
    }
}
