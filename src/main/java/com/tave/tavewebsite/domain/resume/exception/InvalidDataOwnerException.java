package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.INVALID_DATA_OWNER;

public class InvalidDataOwnerException extends BaseErrorException {
    public InvalidDataOwnerException() {
        super(INVALID_DATA_OWNER.getCode(), INVALID_DATA_OWNER.getMessage());
    }
}
