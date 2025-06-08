package com.tave.tavewebsite.domain.interviewplace.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record InterviewPlaceSaveDto(

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate interviewDay,
        @NotBlank String generalAddress,
        @NotBlank String detailAddress,
        @NotBlank String openChatLink,
        @NotBlank String code
) {
}
