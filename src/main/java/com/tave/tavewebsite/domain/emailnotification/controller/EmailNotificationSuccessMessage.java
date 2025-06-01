package com.tave.tavewebsite.domain.emailnotification.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EmailNotificationSuccessMessage {
    POST_SUCCESS("이메일 알림 신청에 성공했습니다."),
    GET_SUCCESS("신규 회원 모집 알림 신청자 조회에 성공했습니다."),
    APPLY_EMAIL_SEND_SUCCESS("신규 회원 모집 알림 이메일 전송이 완료되었습니다."),
    APPLY_EMAIL_BATCH_JOB_RESERVE("신규 회원 모집 알림 이메일 발송 설정이 완료되었습니다. 작업은 03:00에 실행됩니다.");

    private final String message;

    public String getMessage() {
        return message;
    }
}

