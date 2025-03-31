package com.tave.tavewebsite.domain.history.dto.response;

import com.tave.tavewebsite.domain.history.entity.History;
import java.time.LocalDateTime;

public record HistoryResponseDto(
        Long id,
        String description,
        String additionalDescription,
        LocalDateTime lastUpdated,
        Boolean isPublic
) {
    public static HistoryResponseDto of(History history) {
        return new HistoryResponseDto(history.getId(), history.getDescription(),
                history.getAdditionalDescription(), history.getUpdatedAt(), history.isPublic());
    }
}
