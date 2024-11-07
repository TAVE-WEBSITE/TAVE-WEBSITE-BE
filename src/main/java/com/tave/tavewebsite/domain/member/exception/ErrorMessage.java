package com.tave.tavewebsite.domain.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    NOT_FOUNT_USER(400, "유저를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(400, "이메일이 중복됩니다."),
    DUPLICATE_NICKNAME(400, "이미 사용중인 아이디입니다. 다른 아이디로 가입해주세요.");

    final int code;
    final String message;
}
