package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage.INVALID_EMAIL_FORMAT;

public class InvalidEmailFormatException extends BaseErrorException {
    public InvalidEmailFormatException() {
        super(INVALID_EMAIL_FORMAT.getCode(), INVALID_EMAIL_FORMAT.getMessage());
    }
}
