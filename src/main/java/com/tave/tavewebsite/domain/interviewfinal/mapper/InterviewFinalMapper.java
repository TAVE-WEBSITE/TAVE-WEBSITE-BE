package com.tave.tavewebsite.domain.interviewfinal.mapper;

import com.tave.tavewebsite.domain.interviewfinal.dto.response.InterviewFinalForMemberDto;
import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import com.tave.tavewebsite.domain.interviewplace.dto.response.InterviewPlaceDetailDto;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class InterviewFinalMapper {

    // InterviewFinalForMemberDto <--- (InterviewFinal, InterviewPlaceDetailDto)
    public InterviewFinalForMemberDto mapInterviewFinalForMember(InterviewFinal interviewFinal, InterviewPlaceDetailDto detailDto) {

        LocalDate date = interviewFinal.getInterviewDate();
        LocalTime time = interviewFinal.getInterviewTime();

        // yyyy-MM-dd와 HH:mm으로 Date, Time 포맷
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));

        // 한글로 요일 변환
        String dayName = convertToKoreanDay(date.getDayOfWeek());

        return InterviewFinalForMemberDto.of(formattedDate, formattedTime, dayName, detailDto);
    }

    private String convertToKoreanDay(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "월요일";
            case TUESDAY -> "화요일";
            case WEDNESDAY -> "수요일";
            case THURSDAY -> "목요일";
            case FRIDAY -> "금요일";
            case SATURDAY -> "토요일";
            case SUNDAY -> "일요일";
        };
    }
}
