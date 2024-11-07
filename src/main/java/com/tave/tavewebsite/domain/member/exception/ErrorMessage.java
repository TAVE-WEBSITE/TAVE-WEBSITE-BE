package com.tave.tavewebsite.domain.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    NOT_FOUNT_USER(400, "유저를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(400, "이메일이 중복됩니다."),
    DUPLICATE_NICKNAME(400, "ID가 중복됩니다.");

    final int code;
    final String message;
}
