package com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable;

import com.tave.tavewebsite.domain.interviewfinal.dto.response.InterviewFinalDetailDto;
import lombok.Builder;

import java.util.List;

@Builder
public record InterviewTimeTableDto(

        TotalDateTimeDto totalDateTimeDto,
        List<InterviewTimeTableGroupByDayDto> timetableList
) {
    public static InterviewTimeTableDto of(TotalDateTimeDto totalDateTimeDto, List<InterviewTimeTableGroupByDayDto> timetableList) {
        return InterviewTimeTableDto.builder()
                .totalDateTimeDto(totalDateTimeDto)
                .timetableList(timetableList)
                .build();
    }
}
