package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.RESUME_ANSWER_TEXT_LENGTH_OVER;

public class AnswerTextLengthOverException extends BaseErrorException {
    public AnswerTextLengthOverException() {
        super(400, RESUME_ANSWER_TEXT_LENGTH_OVER.getMessage());
    }
}
