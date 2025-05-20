package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.TEMP_SAVE_FAILED;

public class TempSaveFailedException extends BaseErrorException {
    public TempSaveFailedException() {
        super(TEMP_SAVE_FAILED.getCode(), TEMP_SAVE_FAILED.getMessage());
    }
}
