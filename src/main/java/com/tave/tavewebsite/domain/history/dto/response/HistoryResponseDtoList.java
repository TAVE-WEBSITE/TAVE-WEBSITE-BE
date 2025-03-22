package com.tave.tavewebsite.domain.history.dto.response;

import java.util.List;

public record HistoryResponseDtoList(
        String generation,
        List<HistoryResponseDto> details
) {
    public static HistoryResponseDtoList of(String generation, List<HistoryResponseDto> result) {
        return new HistoryResponseDtoList(generation, result);
    }
}
