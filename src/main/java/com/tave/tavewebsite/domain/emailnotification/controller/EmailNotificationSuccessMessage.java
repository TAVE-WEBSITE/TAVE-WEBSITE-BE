package com.tave.tavewebsite.domain.emailnotification.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EmailNotificationSuccessMessage {
    POST_SUCCESS("이메일 알림 신청에 성공했습니다.");

    private final String message;

    public String getMessage() {
        return message;
    }
}

