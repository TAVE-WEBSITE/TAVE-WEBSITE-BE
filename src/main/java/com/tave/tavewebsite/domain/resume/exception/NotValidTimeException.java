package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.NOT_VALID_TIME;

public class NotValidTimeException extends BaseErrorException {
    public NotValidTimeException() {
        super(NOT_VALID_TIME.getCode(), NOT_VALID_TIME.getMessage());
    }
}
