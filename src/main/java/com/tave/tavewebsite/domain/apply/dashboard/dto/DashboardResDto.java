package com.tave.tavewebsite.domain.apply.dashboard.dto;

import java.util.List;

public record DashboardResDto(
        long totalCount,
        double comparisonRatio,
        long temperCount,
        List<DashboardRatioResDto> sexRatioDtos,
        List<DashboardRatioResDto> fieldRatioDtos,
        String generation
) {
}
