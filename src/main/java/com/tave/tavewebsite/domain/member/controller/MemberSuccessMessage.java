package com.tave.tavewebsite.domain.member.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SuccessMessage {
    UNAUTHORIZED_MEMBER_READ("승인 대기 중인 운영진을 조회했습니다."),
    AUTHORIZED_MEMBER_READ("승인된 운영진을 조회했습니다."),
    MANAGER_DELETE("해당 운영진을 삭제했습니다.");

    private final String message;

    public String getMessage(String generation) {
        return generation +" " + message;
    }

    public String getMessage() {
        return message;
    }
}
