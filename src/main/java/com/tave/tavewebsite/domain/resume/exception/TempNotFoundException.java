package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.TEMP_NOT_FOUND;

public class TempNotFoundException extends BaseErrorException {
    public TempNotFoundException() {
        super(TEMP_NOT_FOUND.getCode(), TEMP_NOT_FOUND.getMessage());
    }
}