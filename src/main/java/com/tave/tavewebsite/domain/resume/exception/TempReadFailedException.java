package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.TEMP_READ_FAILED;

public class TempReadFailedException extends BaseErrorException {
    public TempReadFailedException() {
        super(TEMP_READ_FAILED.getCode(), TEMP_READ_FAILED.getMessage());
    }
}
