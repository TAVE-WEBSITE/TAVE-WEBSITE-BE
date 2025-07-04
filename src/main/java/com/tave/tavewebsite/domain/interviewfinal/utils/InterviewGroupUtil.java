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

    public List<InterviewTimeTableGroupByDayDto> createTimeTableDtoList(
            Map<LocalDate, Map<LocalTime, List<InterviewFinal>>>  group
    ) {

        List<InterviewTimeTableGroupByDayDto> groupByDayList = new ArrayList<>();

        // GroupByDate 로 순회
        for(LocalDate date : getGroupKeyByDate(group)){
            Map<LocalTime, List<InterviewFinal>> timeMap = group.get(date);

            String groupByDay = date.toString();
            String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);

            List<InterviewTimeTableGroupByTimeDto> groupByTimeList = new ArrayList<>();

            // GroupByDate In GroupByTime 으로 순회
            for (LocalTime groupTime : getGroupKeyByTime(timeMap)) {
                List<InterviewFinal> interviewListAtTime = timeMap.get(groupTime);
                List<TimeTableMemberDetailDto> memberDtoList = new ArrayList<>();

                // M/d, HH:MM에 면접 예정인 면접 정보(InterviewFinal)을 Dto 변환
                for (InterviewFinal interviewFinal : interviewListAtTime) {
                    memberDtoList.add(TimeTableMemberDetailDto.from(interviewFinal));
                }

                groupByTimeList.add(InterviewTimeTableGroupByTimeDto.from(memberDtoList, groupTime));
            }

            // 하루 단위 DTO 생성
            groupByDayList.add(InterviewTimeTableGroupByDayDto
                    .of(groupByDay, dayName, groupByTimeList));
        }

        return groupByDayList;
    }

    // NOTE 이 정보를 InterviewTime 에서 가져와야할 지 고민
    public TotalDateTimeDto getTotalDateTimeDto(
            Map<LocalDate, Map<LocalTime, List<InterviewFinal>>> group
    ) {
        List<LocalDate> dateList = group.keySet().stream().toList();
        List<LocalTime> timeList = group.get(dateList.get(0)).keySet().stream().toList();

        return TotalDateTimeDto.of(dateList, timeList);
    }


    private List<LocalDate> getGroupKeyByDate(Map<LocalDate, Map<LocalTime, List<InterviewFinal>>> group) {
        return group.keySet().stream()
                .sorted()
                .toList();
    }

    private List<LocalTime> getGroupKeyByTime(Map<LocalTime, List<InterviewFinal>> timeMap) {
        return timeMap.keySet().stream()
                .sorted()
                .toList();
    }

}
