package com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable;

import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
public record InterviewTimeTableGroupByTimeDto(

        String groupByTime,
        List<TimeTableMemberDetailDto> memberDtoList

) {
    public static InterviewTimeTableGroupByTimeDto from(List<TimeTableMemberDetailDto> memberDtoList, LocalTime localTime) {

        String groupByTime = localTime.toString();

        return InterviewTimeTableGroupByTimeDto.builder()
                .groupByTime(groupByTime)
                .memberDtoList(memberDtoList)
                .build();
    }
}
