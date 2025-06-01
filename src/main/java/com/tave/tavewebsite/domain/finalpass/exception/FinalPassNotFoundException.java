package com.tave.tavewebsite.domain.finalpass.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.finalpass.exception.ErrorMessage.FINAL_PASS_NOT_FOUND;

public class FinalPassNotFoundException extends BaseErrorException {
    public FinalPassNotFoundException() {
        super(FINAL_PASS_NOT_FOUND.getCode(), FINAL_PASS_NOT_FOUND.getMessage());
    }
}
