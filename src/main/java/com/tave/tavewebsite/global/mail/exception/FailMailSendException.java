package com.tave.tavewebsite.global.mail.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.global.mail.exception.ErrorMessage.FAIL_SEND_MAIL;

public class FailMailSendException extends BaseErrorException {
    public FailMailSendException() {
        super(FAIL_SEND_MAIL.getCode(), FAIL_SEND_MAIL.getMessage());
    }
}
