package com.tave.tavewebsite.domain.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    NOT_FOUNT_USER(400, "유저를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(400, "중복되는 이메일입니다."),
    DUPLICATE_NICKNAME(400, "이미 사용중인 아이디입니다. 다른 아이디로 가입해주세요."),
    _NOT_MATCHED_VERIFIED_NUMBER(400, "인증 번호가 일치하지 않습니다."),
    _EXPIRED_NUMBER(401, "인증번호의 만료시간이 지났습니다."),
    _NOT_MATCHED_PASSWORD(400, "비밀번호가 일치하지 않습니다."),
    NOT_MANAGER(400, "MANAGER이 아닙니다."),
    INVALID_STATUS_VALUE(400, "유효하지 않은 상태 값입니다."),
    NOT_FOUND_UNAUTHORIZED_MANAGER(400, "대기 중인 운영진을 찾을 수 없습니다."),
    UNAUTHORIZED_MANAGER(400, "아직 승인되지 않은 운영진입니다."),
    INVALID_EMAIL_FORMAT(400, "이메일 형식이 올바르지 않습니다.");

    final int code;
    final String message;
}
