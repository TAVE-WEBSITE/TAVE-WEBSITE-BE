package com.tave.tavewebsite.domain.member.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SuccessMessage {
    UNAUTHORIZED_MEMBER_READ("승인 대기 중인 운영진을 조회했습니다."),
    AUTHORIZED_MEMBER_READ("승인된 운영진을 조회했습니다."),
    MANAGER_DELETE("해당 운영진을 삭제했습니다."),
    ALL_MANAGER_READ("모든 운영진을 조회했습니다."),

    SIGN_OUT_SUCCESS("로그아웃에 성공했습니다."),
    DELETE_MEMBER_SUCCESS("멤버 삭제에 성공했습니다."),

    SEND_AUTHENTICATION_CODE("인증번호 발송에 성공했습니다."),
    VERIFY_SUCCESS_MESSAGE("인증에 성공했습니다."),
    CAN_USE_NICKNAME("닉네임 사용 가능합니다."),
    RESET_PASSWORD_MESSAGE("비밀번호가 재설정되었습니다.\n 다시 로그인해주세요.");

    private final String message;

    public String getMessage(String generation) {
        return generation + " " + message;
    }

    public String getMessage() {
        return message;
    }
}