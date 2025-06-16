package com.tave.tavewebsite.domain.interviewfinal.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.interviewfinal.exception.ErrorMessage.NOT_FOUND_INTERVIEW_FINAL_BY_MEMBER;

public class NotFoundInterviewFinalByMemberIdException extends BaseErrorException {
    public NotFoundInterviewFinalByMemberIdException() {
        super(NOT_FOUND_INTERVIEW_FINAL_BY_MEMBER.getCode(), NOT_FOUND_INTERVIEW_FINAL_BY_MEMBER.getMessage());
    }
}
