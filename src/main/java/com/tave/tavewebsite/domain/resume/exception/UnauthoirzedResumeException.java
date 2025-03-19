package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.UNAUTHORIZED_RESUME;

public class UnauthoirzedResumeException extends BaseErrorException {
    public UnauthoirzedResumeException() {
        super(UNAUTHORIZED_RESUME.getCode(), UNAUTHORIZED_RESUME.getMessage());
    }
}
