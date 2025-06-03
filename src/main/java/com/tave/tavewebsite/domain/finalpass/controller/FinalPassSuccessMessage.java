package com.tave.tavewebsite.domain.finalpass.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FinalPassSuccessMessage {
    CREATE_FINAL_PASS("최종합격 안내 설정에 성공했습니다."),
    READ_FINAL_PASS("최종합격 안내 조회에 성공했습니다.");

    private final String message;
}
