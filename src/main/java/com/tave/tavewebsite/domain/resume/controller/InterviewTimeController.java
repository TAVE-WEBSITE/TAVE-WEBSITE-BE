package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.request.InterviewTimeReqDto;
import com.tave.tavewebsite.domain.resume.dto.response.InterviewTimeResponseDto;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotResDto;
import com.tave.tavewebsite.domain.resume.service.InterviewTimeService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tave.tavewebsite.domain.resume.controller.AdminConfigSuccessMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class InterviewTimeController {

    private final InterviewTimeService interviewTimeService;

    @PostMapping("/admin/config/interview-time")
    public SuccessResponse createInterviewTimeConfig(@RequestBody @Valid InterviewTimeReqDto reqDto) {

        interviewTimeService.createInterviewTime(reqDto);

        return new SuccessResponse(CREATE_INTERVIEW_TIME_SUCCESS.getMessage());
    }

    @GetMapping("/manager/config/interview-time")
    public SuccessResponse<List<InterviewTimeResponseDto>> getInterviewTimeDay() {

        List<InterviewTimeResponseDto> response = interviewTimeService.distinctInterviewDay();

        return new SuccessResponse<>(response, GET_DISTINCT_INTERVIEW_TIME_DAY.getMessage());
    }

    @GetMapping("/normal/config/interview-time")
    public SuccessResponse<List<TimeSlotResDto>> getInterviewTimeList() {

        List<TimeSlotResDto> response = interviewTimeService.getAllInterviewTimes();

        return new SuccessResponse<>(response, GET_ALL_INTERVIEW_DATE_TIME.getMessage());
    }

}
