package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.service.ResumeTimeService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member/info/{resumeId}/timeslot")
public class TimeSlotController {

    private final ResumeTimeService resumeTimeService;

//    @PostMapping
//    public SuccessResponse createTimeSlot(@PathVariable("resumeId") Long resumeId,
//                                          @RequestBody @Valid List<TimeSlotReqDto> timeSlots){
//
//        resumeTimeService.createTimeSlot(resumeId, timeSlots);
//        return SuccessResponse.ok(TimeSlotSuccessMessage.UPLOAD_SUCCESS.getMessage());
//    }

    @GetMapping
    public SuccessResponse getTimeSlots(@PathVariable("resumeId") Long resumeId){
        return new SuccessResponse<>(resumeTimeService.getTimeSlots(resumeId),
                TimeSlotSuccessMessage.READ_SUCCESS.getMessage());
    }

//    @PatchMapping
//    public SuccessResponse updateTimeSlot(@PathVariable("resumeId") Long resumeId,
//                                          @RequestBody @Valid List<TimeSlotReqDto> timeSlots){
//        resumeTimeService.updateTimeSlot(resumeId, timeSlots);
//
//        return SuccessResponse.ok(TimeSlotSuccessMessage.UPDATE_SUCCESS.getMessage());
//    }
}
