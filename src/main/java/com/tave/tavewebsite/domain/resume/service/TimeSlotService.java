package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotResDto;
import com.tave.tavewebsite.domain.resume.entity.TimeSlot;

import java.util.List;

public interface TimeSlotService {

    void createTimeSlot(TimeSlot timeSlot);
    void updateTimeSlot(TimeSlot timeSlot);
    List<TimeSlotResDto> getTimeSlots(String timeSlotId);
}
