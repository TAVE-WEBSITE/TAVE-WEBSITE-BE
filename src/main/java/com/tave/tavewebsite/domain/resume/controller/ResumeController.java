package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.resume.dto.response.ResumeListResponse;
import com.tave.tavewebsite.domain.resume.usecase.ResumeUseCase;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeUseCase resumeUseCase;

    @GetMapping("/v1/normal/resume/interview-time")
    public SuccessResponse getResumeListToInterviewTime(
            @RequestParam("date")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate date,

            @RequestParam("time")
            @DateTimeFormat(pattern = "HH:mm")
            LocalTime time
    ){

        ResumeListResponse response = resumeUseCase.getResumeByInterviewDateTime(date, time);

        return new SuccessResponse(response, "");
    }
}
