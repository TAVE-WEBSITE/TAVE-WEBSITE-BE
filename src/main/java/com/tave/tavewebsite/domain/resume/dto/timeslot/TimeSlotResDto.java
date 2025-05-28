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

        LocalDate date = getLocalDate(resumeTimeSlot.getInterviewTime().getInterviewYear(), resumeTimeSlot.getInterviewTime().getInterviewDate());
        LocalTime time = getLocalTime(resumeTimeSlot.getInterviewTime().getInterviewTime());
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        return new TimeSlotResDto(localDateTime);
    }

    public static TimeSlotResDto fromInterviewTime(InterviewTime interviewTime) {
        LocalDate date = getLocalDate(interviewTime.getInterviewYear(), interviewTime.getInterviewDate());
        LocalTime time = getLocalTime(interviewTime.getInterviewTime());
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        return new TimeSlotResDto(localDateTime);
    }

    private static LocalDate getLocalDate(String currentYear, String currentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(currentYear+"/"+currentDate, formatter);
    }

    private static LocalTime getLocalTime(String localTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.parse(localTime, formatter);
        return time;
    }
}
