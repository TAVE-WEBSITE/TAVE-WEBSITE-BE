package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record InterviewTimeResponseDto(
        String formattedDay,
        LocalDate originalDate
) {
    public static InterviewTimeResponseDto of(String formattedDay, LocalDate originalDate) {
        return InterviewTimeResponseDto.builder()
                .formattedDay(formattedDay)
                .originalDate(originalDate)
                .build();
    }
}
