package com.tave.tavewebsite.global.mail.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.global.mail.exception.ErrorMessage.FAIL_CREATE_MAIL;

public class FailCreateMailException extends BaseErrorException {
    public FailCreateMailException() {
        super(FAIL_CREATE_MAIL.getCode(), FAIL_CREATE_MAIL.getMessage());
    }
}
