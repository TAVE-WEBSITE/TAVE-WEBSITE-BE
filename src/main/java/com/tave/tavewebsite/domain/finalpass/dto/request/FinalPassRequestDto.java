package com.tave.tavewebsite.domain.finalpass.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FinalPassRequestDto {
    private Integer totalFee;
    private Integer clubFee;
    private Integer mtFee;
    private LocalDate feeDeadline;
    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private String surveyLink;
    private LocalDate surveyDeadline;
    private String otLink;
    private String otPassword;
    private LocalDate otDeadline;
}