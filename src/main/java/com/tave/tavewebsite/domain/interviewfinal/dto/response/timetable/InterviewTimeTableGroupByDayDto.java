package com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable;

import lombok.Builder;

import java.util.List;

@Builder
public record InterviewTimeTableGroupByDayDto(

        String groupByDay,
        String dayName,
        List<InterviewTimeTableGroupByTimeDto> groupByTimeDtoList


) {
    public static InterviewTimeTableGroupByDayDto of(String groupByDay, String dayName,List<InterviewTimeTableGroupByTimeDto> dtoList) {
        return InterviewTimeTableGroupByDayDto.builder()
                .groupByDay(groupByDay)
                .dayName(dayName)
                .groupByTimeDtoList(dtoList)
                .build();
    }
}
