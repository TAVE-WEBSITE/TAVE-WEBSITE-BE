package com.tave.tavewebsite.domain.finalpass.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FinalPassResponseDto {

    private Long id;
    private Integer totalFee;
    private Integer clubFee;
    private Integer mtFee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime feeDeadline;

    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private String surveyLink;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime surveyDeadline;

    private String otLink;
    private String otPassword;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime otDeadline;
}