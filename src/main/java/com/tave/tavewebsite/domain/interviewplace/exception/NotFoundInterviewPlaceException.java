package com.tave.tavewebsite.domain.interviewplace.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.interviewplace.exception.ErrorMessage.NOT_FOUNT_INTERVIEW_PLACE;

public class NotFoundInterviewPlaceException extends BaseErrorException {
    public NotFoundInterviewPlaceException() {
        super(NOT_FOUNT_INTERVIEW_PLACE.getCode(), NOT_FOUNT_INTERVIEW_PLACE.getMessage());
    }
}
