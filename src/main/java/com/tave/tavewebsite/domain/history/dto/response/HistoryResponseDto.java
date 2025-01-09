package com.tave.tavewebsite.domain.history.dto.response;

import com.tave.tavewebsite.domain.history.entity.History;

public record HistoryResponseDto(
        Long id,
        String description,
        String additionalDescription,
        Boolean isPublic
) {
    public static HistoryResponseDto of(History history) {
        return new HistoryResponseDto(history.getId(), history.getDescription(),
                history.getAdditionalDescription(), history.isPublic());
    }
}
