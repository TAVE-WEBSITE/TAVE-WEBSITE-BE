package com.tave.tavewebsite.global.security.exception;

import lombok.Getter;

@Getter
public enum JwtErrorMessage {

    INVALID_JWT_TOKEN(401, "유효하지 않은 JWT 토큰 입니다."),
    EXPIRED_JWT_TOKEN(401, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(401, "지원하지 않는 유형의 JWT 토큰입니다."),
    CLAIMS_IS_EMPTY(401, "Claims 내부에 값이 들어있지 않습니다.");

    private final int errorCode;
    private final String message;

    JwtErrorMessage(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}
