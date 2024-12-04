package com.tave.tavewebsite.global.security.exception;

import lombok.Getter;

@Getter
public enum JwtErrorMessage {

    INVALID_JWT_TOKEN(401, "유효하지 않은 JWT 토큰 입니다."),
    NEED_ACCESS_TOKEN_REFRESH(401, "토큰 재발급이 필요합니다."),
    CANNOT_USE_REFRESH_TOKEN(400, "리프레쉬 토큰은 사용할 수 없습니다."),
    UNSUPPORTED_JWT_TOKEN(401, "지원하지 않는 유형의 JWT 토큰입니다."),
    NOT_MATCH_REFRESH_TOKEN(400, "로그인이 필요한 서비스입니다."),
    SIGN_OUT_USER(400, "이미 로그아웃한 사용자 입니다."),
    CLAIMS_IS_EMPTY(401, "Claims 내부에 값이 들어있지 않습니다.");

    private final int errorCode;
    private final String message;

    JwtErrorMessage(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}
