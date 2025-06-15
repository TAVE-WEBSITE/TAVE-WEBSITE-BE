package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record InterviewTimeResponseDto(
        String formattedDate,
        LocalDate originalDate
) {
    public static InterviewTimeResponseDto of(String formattedDate, LocalDate originalDate) {
        return InterviewTimeResponseDto.builder()
                .formattedDate(formattedDate)
                .originalDate(originalDate)
                .build();
    }
}
