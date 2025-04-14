package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotReqDto;
import com.tave.tavewebsite.domain.resume.service.TimeSlotService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member/info/{resumeId}/timeslot")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @PostMapping
    public SuccessResponse createTimeSlot(@PathVariable("resumeId") Long resumeId,
                                          @RequestBody @Valid List<TimeSlotReqDto> timeSlots){

        timeSlotService.createTimeSlot(resumeId, timeSlots);
        return SuccessResponse.ok(TimeSlotSuccessMessage.UPLOAD_SUCCESS.getMessage());
    }

    @GetMapping
    public SuccessResponse getTimeSlots(@PathVariable("resumeId") Long resumeId){
        return new SuccessResponse<>(timeSlotService.getTimeSlots(resumeId),
                TimeSlotSuccessMessage.READ_SUCCESS.getMessage());
    }

    @PatchMapping
    public SuccessResponse updateTimeSlot(@PathVariable("resumeId") Long resumeId,
                                          @RequestBody @Valid List<TimeSlotReqDto> timeSlots){
        timeSlotService.updateTimeSlot(resumeId, timeSlots);

        return SuccessResponse.ok(TimeSlotSuccessMessage.UPDATE_SUCCESS.getMessage());
    }
}
