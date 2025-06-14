package com.tave.tavewebsite.domain.applicant.history.dto.response;

public record ApplicantHistoryResponseDto(
        String generation,
        String fieldType,
        String applicationStatus
) {
}
