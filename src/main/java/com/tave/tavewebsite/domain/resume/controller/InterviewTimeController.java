package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.request.InterviewTimeReqDto;
import com.tave.tavewebsite.domain.resume.service.InterviewTimeService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tave.tavewebsite.domain.resume.controller.AdminConfigSuccessMessage.CREATE_INTERVIEW_TIME_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/config")
public class InterviewTimeController {

    private final InterviewTimeService interviewTimeService;

    @PostMapping("/interview")
    public SuccessResponse createInterviewTimeConfig(@RequestBody @Valid InterviewTimeReqDto reqDto) {

        interviewTimeService.createInterviewTime(reqDto);

        return new SuccessResponse(CREATE_INTERVIEW_TIME_SUCCESS.getMessage());
    }
}
