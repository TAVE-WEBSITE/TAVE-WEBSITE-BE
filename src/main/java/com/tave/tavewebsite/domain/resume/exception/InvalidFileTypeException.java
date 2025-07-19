package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.FILE_TYPE_INVALID;

public class InvalidFileTypeException extends BaseErrorException {
    public InvalidFileTypeException() {
        super(FILE_TYPE_INVALID.getCode(), FILE_TYPE_INVALID.getMessage());
    }
}
