package com.tave.tavewebsite.domain.session.exception;

import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    SESSION_NOT_FOUND(400, "세션을 찾을 수 없습니다.");

    private final int code;
    private final String message;
}
