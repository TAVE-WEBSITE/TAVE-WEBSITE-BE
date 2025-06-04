package com.tave.tavewebsite.domain.resume.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InterviewTimeReqDto(

        @NotNull(message = "yyyy-MM-dd 형식을 맞추어주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @NotNull(message = "yyyy-MM-dd 형식을 맞추어주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @NotEmpty(message = "HH:mm 형식의 String 값을 전송해주세요.")
        String startTime,

        @NotEmpty(message = "HH:mm 형식의 String 값을 전송해주세요.")
        String endTime,

        int progressTime
) {
}
