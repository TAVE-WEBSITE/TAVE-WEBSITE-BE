package com.tave.tavewebsite.domain.interviewfinal.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.interviewfinal.exception.ErrorMessage.EMPTY_FILE_EXCEPTION;

public class EmptyFileException extends BaseErrorException {
    public EmptyFileException() {
        super(EMPTY_FILE_EXCEPTION.getCode(), EMPTY_FILE_EXCEPTION.getMessage());
    }
}
