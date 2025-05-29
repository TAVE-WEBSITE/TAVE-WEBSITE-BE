package com.tave.tavewebsite.domain.finalpass.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FinalPassRequestDto {
    @NotNull(message = "총 회비 필수로 입력해주세요.")
    private Integer totalFee;

    private Integer clubFee;

    private Integer mtFee;

    @NotNull(message = "회비 납부 마감기한 필수로 입력해주세요.")
    private LocalDate feeDeadline;

    @NotNull(message = "은행명 필수로 입력해주세요.")
    private String bankName;

    @NotNull(message = "계좌번호 필수로 입력해주세요.")
    private String accountNumber;

    @NotNull(message = "예금주 필수로 입력해주세요.")
    private String accountHolder;

    @NotNull(message = "아지트 초대 설문 조사 링크 필수로 입력해주세요.")
    private String surveyLink;

    @NotNull(message = "아지트 초대 설문 조사 마감기한 필수로 입력해주세요.")
    private LocalDate surveyDeadline;

    @NotNull(message = "OT 공지방 링크 필수로 입력해주세요.")
    private String otLink;

    @NotNull(message = "OT 공지방 비밀번호 필수로 입력해주세요.")
    private String otPassword;

    @NotNull(message = "OT 마감기한 필수로 입력해주세요. ")
    private LocalDate otDeadline;
}