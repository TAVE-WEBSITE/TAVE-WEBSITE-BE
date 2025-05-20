package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.INVALID_PAGE_NUMBER;

public class InvalidPageNumberException extends BaseErrorException {
    public InvalidPageNumberException() {
        super(INVALID_PAGE_NUMBER.getCode(), INVALID_PAGE_NUMBER.getMessage());
    }
}
