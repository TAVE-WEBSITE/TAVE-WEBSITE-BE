package com.tave.tavewebsite.domain.interviewfinal.utils;

import com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable.InterviewTimeTableGroupByDayDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable.InterviewTimeTableGroupByTimeDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable.TimeTableMemberDetailDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable.TotalDateTimeDto;
import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InterviewGroupUtil {

    public Map<LocalDate, Map<LocalTime, List<InterviewFinal>>> groupByDateAndTime(List<InterviewFinal> interviewFinalList) {
        return interviewFinalList.stream()
                .collect(Collectors.groupingBy(
                        InterviewFinal::getInterviewDate,
                        Collectors.groupingBy(InterviewFinal::getInterviewTime)
                ));
    }

}
