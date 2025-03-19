package com.tave.tavewebsite.global.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    NOT_AUTHORIZED(403, "회원 정보가 유효하지 않습니다.");

    private final int code;
    private final String message;
}
