package com.tave.tavewebsite.domain.apply.dashboard.dto;

public record DashboardUpdateDto(
        Long totalCount,
        Long maleCount,
        Long femaleCount,
        Long backendCount,
        Long webFrontCount,
        Long designCount,
        Long appFrontCount,
        Long dataAnalysisCount,
        Long deepCount

) {
}
