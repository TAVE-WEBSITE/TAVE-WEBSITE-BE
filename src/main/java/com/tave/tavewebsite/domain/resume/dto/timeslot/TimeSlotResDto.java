package com.tave.tavewebsite.domain.resume.dto.timeslot;

import com.tave.tavewebsite.domain.resume.entity.TimeSlot;

import java.time.LocalDateTime;

public record TimeSlotResDto(
        LocalDateTime time
) {
    public static TimeSlotResDto from(TimeSlot timeSlot) {
        return new TimeSlotResDto(timeSlot.getTime());
    }
}
