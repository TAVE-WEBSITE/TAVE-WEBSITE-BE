package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.RESUME_NOT_FOUND;

public class ResumeNotFoundException extends BaseErrorException {
    public ResumeNotFoundException() {
        super(RESUME_NOT_FOUND.getCode(), RESUME_NOT_FOUND.getMessage());
    }
}
