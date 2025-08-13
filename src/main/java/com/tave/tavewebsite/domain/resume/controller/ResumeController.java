package com.tave.tavewebsite.domain.resume.controller;

import com.tave.tavewebsite.domain.interviewfinal.dto.S3FileInputStreamDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeListResponse;
import com.tave.tavewebsite.domain.resume.usecase.ResumeUseCase;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.tave.tavewebsite.domain.resume.controller.message.ResumeSuccessMesssage.RESUME_GET_TO_DATE_AND_TIME_SUCCESS;

@RestController
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeUseCase resumeUseCase;

    @GetMapping("/v1/manager/resume/interview-time")
    public SuccessResponse getResumeListToInterviewTime(
            @RequestParam("date")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate date,

            @RequestParam("time")
            @DateTimeFormat(pattern = "HH:mm")
            LocalTime time
    ){

        ResumeListResponse response = resumeUseCase.getResumeByInterviewDateTime(date, time);

        return new SuccessResponse(response, RESUME_GET_TO_DATE_AND_TIME_SUCCESS.getMessage());
    }

    @GetMapping("/v1/manager/resume/portfolio/{resumeId}")
    public ResponseEntity<InputStreamResource> downloadPortfolio(
            @PathVariable("resumeId") Long resumeId
    ) throws IOException {

        S3FileInputStreamDto response = resumeUseCase.downloadPortfolio(resumeId);

        return ResponseEntity.ok()
                .headers(response.headers())
                .contentLength(response.contentLength())
                .body(response.inputStreamResource());
    }

}
