package com.tave.tavewebsite.domain.resume.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record InterviewTimeReqDto(

        @NotEmpty(message = "HH:mm 형식의 String 값을 전송해주세요.")
        String startTime,

        @NotEmpty(message = "HH:mm 형식의 String 값을 전송해주세요.")
        String endTime,

        int progressTime
) {
}
