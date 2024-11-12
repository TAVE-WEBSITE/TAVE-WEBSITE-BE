package com.tave.tavewebsite.global.mail.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.codec.net.BCodec;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    FAIL_SEND_MAIL(500, "메일 전송에 실패했습니다."),
    FAIL_CREATE_MAIL(500, "메일 생성 중 오류가 발생했습니다.");

    private final int code;
    private final String message;
}
