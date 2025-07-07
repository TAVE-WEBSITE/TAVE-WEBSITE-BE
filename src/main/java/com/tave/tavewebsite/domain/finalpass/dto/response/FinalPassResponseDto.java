package com.tave.tavewebsite.domain.finalpass.dto.response;

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
    private LocalDateTime feeDeadline;
    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private String surveyLink;
    private LocalDateTime surveyDeadline;
    private String otLink;
    private String otPassword;
    private LocalDateTime otDeadline;
}