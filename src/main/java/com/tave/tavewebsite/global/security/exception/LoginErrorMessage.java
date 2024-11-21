package com.tave.tavewebsite.global.security.exception;

import lombok.Getter;

@Getter
public enum LoginErrorMessage {

    EMAIL_NOT_FOUND(400, "아이디 혹은 비밀번호가 일치하지 않습니다."),
    AUTHENTICATION_NOT_FOUND(400, "아이디 혹은 비밀번호가 일치하지 않습니다.");


    private final int errorCode;
    private final String message;

    LoginErrorMessage(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}
