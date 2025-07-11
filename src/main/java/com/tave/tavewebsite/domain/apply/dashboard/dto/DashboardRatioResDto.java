package com.tave.tavewebsite.domain.apply.dashboard.dto;

public record DashboardRatioResDto(
        String topic,
        Long count,
        double ratio
) {
}
