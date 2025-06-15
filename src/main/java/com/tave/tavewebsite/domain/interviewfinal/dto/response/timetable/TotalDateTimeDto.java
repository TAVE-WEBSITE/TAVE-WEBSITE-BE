package com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record TotalDateTimeDto(
        List<String> dateList,
        List<String> timeList,
        int dateListSize,
        int timeListSize
) {
    public static TotalDateTimeDto of(List<LocalDate> dateList, List<LocalTime> timeList) {
        return TotalDateTimeDto.builder()
                .dateList(dateList.stream().map(LocalDate::toString).sorted().toList())
                .dateListSize(dateList.size())
                .timeList(timeList.stream().map(LocalTime::toString).sorted().toList())
                .timeListSize(timeList.size())
                .build();
    }
}
