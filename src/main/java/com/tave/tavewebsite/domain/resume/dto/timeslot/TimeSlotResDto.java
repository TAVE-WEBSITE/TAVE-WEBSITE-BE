package com.tave.tavewebsite.domain.resume.dto.timeslot;

import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import com.tave.tavewebsite.domain.resume.entity.ResumeTimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record TimeSlotResDto(
        LocalDateTime time
) {
    public static TimeSlotResDto from(ResumeTimeSlot resumeTimeSlot) {

        LocalDate date = getLocalDate(resumeTimeSlot.getInterviewDetailTime());
        LocalTime time = getLocalTime(resumeTimeSlot.getInterviewDetailTime());
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        return new TimeSlotResDto(localDateTime);
    }

    public static TimeSlotResDto fromInterviewTime(InterviewTime interviewTime) {
        LocalDate date = getLocalDate(interviewTime.getInterviewDetailTime());
        LocalTime time = getLocalTime(interviewTime.getInterviewDetailTime());
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        return new TimeSlotResDto(localDateTime);
    }

    public static TimeSlotResDto from(InterviewTime interviewTime) {
        return new TimeSlotResDto(interviewTime.getInterviewDetailTime());
    }

    private static LocalDate getLocalDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(localDateTime.format(formatter), formatter);
    }

    private static LocalTime getLocalTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(localDateTime.format(formatter), formatter);
    }
}
