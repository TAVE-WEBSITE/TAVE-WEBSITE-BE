package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotReqDto;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotResDto;
import com.tave.tavewebsite.domain.resume.entity.TimeSlot;

import java.util.List;

public interface TimeSlotService {

    void createTimeSlot(Long resumeId, List<TimeSlotReqDto> timeSlots);
    void updateTimeSlot(Long resumeId, List<TimeSlotReqDto> timeSlot);
    List<TimeSlotResDto> getTimeSlots(Long resumeId);
}
