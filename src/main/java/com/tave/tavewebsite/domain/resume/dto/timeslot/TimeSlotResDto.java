package com.tave.tavewebsite.domain.resume.dto.timeslot;

import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import com.tave.tavewebsite.domain.resume.entity.ResumeTimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record TimeSlotResDto(
        LocalDateTime time
) {
    public static TimeSlotResDto from(ResumeTimeSlot resumeTimeSlot) {
        LocalDate date = LocalDate.parse(resumeTimeSlot.getInterviewTime().getInterviewDate());
        LocalTime time = LocalTime.parse(resumeTimeSlot.getInterviewTime().getInterviewTime());
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        return new TimeSlotResDto(localDateTime);
    }

    public static TimeSlotResDto fromInterviewTime(InterviewTime interviewTime) {
        LocalDate date = LocalDate.parse(interviewTime.getInterviewDate());
        LocalTime time = LocalTime.parse(interviewTime.getInterviewTime());
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        return new TimeSlotResDto(localDateTime);
    }
}
