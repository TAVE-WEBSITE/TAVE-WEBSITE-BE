package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.NOT_FOUND_RESUME;

public class ResumeNotFoundException extends BaseErrorException {
    public ResumeNotFoundException() {
        super(NOT_FOUND_RESUME.getCode(), NOT_FOUND_RESUME.getMessage());
    }
}
