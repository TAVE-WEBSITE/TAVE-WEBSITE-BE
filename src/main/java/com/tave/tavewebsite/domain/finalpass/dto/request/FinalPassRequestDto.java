package com.tave.tavewebsite.domain.finalpass.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FinalPassRequestDto {
    @NotNull(message = "총 회비 필수로 입력해주세요.")
    @Positive(message = "총 회비는 0보다 커야 합니다.")
    private Integer totalFee;

    @Positive(message = "동아리 회비는 0보다 커야 합니다.")
    private Integer clubFee;

    @Positive(message = "MT 회비는 0보다 커야 합니다.")
    private Integer mtFee;

    @NotNull(message = "회비 납부 마감기한 필수로 입력해주세요.")
    @FutureOrPresent(message = "회비 납부 마감기한은 오늘 또는 미래여야 합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime feeDeadline;

    @NotBlank(message = "은행명 필수로 입력해주세요.")
    @Size(max = 10, message = "은행명은 10자 이하로 입력해주세요.")
    private String bankName;

    @NotBlank(message = "계좌번호 필수로 입력해주세요.")
    @Size(max = 30, message = "계좌번호는 30자 이하로 입력해주세요.")
    private String accountNumber;

    @NotBlank(message = "예금주 필수로 입력해주세요.")
    @Size(max = 30, message = "예금주는 30자 이하로 입력해주세요.")
    private String accountHolder;

    @NotBlank(message = "아지트 초대 설문 조사 링크 필수로 입력해주세요.")
    @Size(max = 200, message = "링크는 200자 이하로 입력해주세요.")
    private String surveyLink;

    @NotNull(message = "아지트 초대 설문 조사 마감기한 필수로 입력해주세요.")
    @FutureOrPresent(message = "설문 마감기한은 오늘 또는 미래여야 합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime surveyDeadline;

    @NotBlank(message = "OT 공지방 링크 필수로 입력해주세요.")
    @Size(max = 200, message = "링크는 200자 이하로 입력해주세요.")
    private String otLink;

    @NotBlank(message = "OT 공지방 비밀번호 필수로 입력해주세요.")
    @Size(max = 20, message = "비밀번호는 20자 이하로 입력해주세요.")
    private String otPassword;

    @NotNull(message = "OT 마감기한 필수로 입력해주세요. ")
    @FutureOrPresent(message = "OT 마감기한은 오늘 또는 미래여야 합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime otDeadline;
}