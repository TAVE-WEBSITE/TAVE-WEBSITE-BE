package com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable;

import lombok.Builder;

import java.util.List;

@Builder
public record InterviewTimeTableGroupByTimeDto(

        String groupByTime,
        List<TimeTableMemberDetailDto> memberDtoList

) {
    public static InterviewTimeTableGroupByTimeDto from(List<TimeTableMemberDetailDto> memberDtoList) {

        String groupByTime = memberDtoList.get(0).interviewTime();

        return InterviewTimeTableGroupByTimeDto.builder()
                .groupByTime(groupByTime)
                .memberDtoList(memberDtoList)
                .build();
    }
}
