package com.tave.tavewebsite.domain.finalpass.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    FINAL_PASS_NOT_FOUND(404, "최종 합격 안내 정보가 존재하지 않습니다.");

    private final int code;
    private final String message;
}
