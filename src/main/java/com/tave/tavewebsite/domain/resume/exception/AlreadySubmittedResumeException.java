package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.ALREADY_SUBMITTED;

public class AlreadySubmittedResumeException extends BaseErrorException {
    public AlreadySubmittedResumeException() {
        super(ALREADY_SUBMITTED.getCode(), ALREADY_SUBMITTED.getMessage());
    }
}
