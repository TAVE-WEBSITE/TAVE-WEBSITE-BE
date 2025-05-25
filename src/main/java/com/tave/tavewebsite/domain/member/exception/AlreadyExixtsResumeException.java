package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.ALREADY_EXISTS_EVALUATION;

public class AlreadyExixtsResumeException extends BaseErrorException {
    public AlreadyExixtsResumeException() {

        super(ALREADY_EXISTS_EVALUATION.getCode(), ALREADY_EXISTS_EVALUATION.getMessage());
    }
}
