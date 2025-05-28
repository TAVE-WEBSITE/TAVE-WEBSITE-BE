package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.NOT_FOUND_INTERVIEW_TIME;

public class NotFoundInterviewTimeException extends BaseErrorException {
    public NotFoundInterviewTimeException() {
        super(NOT_FOUND_INTERVIEW_TIME.getCode(), NOT_FOUND_INTERVIEW_TIME.getMessage());
    }
}
