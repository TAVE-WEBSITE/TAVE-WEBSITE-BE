package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.INVALID_FIELD;

public class InvalidFieldException extends BaseErrorException {
    public InvalidFieldException() {
      super(INVALID_FIELD.getCode(), INVALID_FIELD.getMessage());
    }
}
