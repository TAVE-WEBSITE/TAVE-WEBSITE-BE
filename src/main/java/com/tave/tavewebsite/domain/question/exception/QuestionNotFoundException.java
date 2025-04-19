package com.tave.tavewebsite.domain.question.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;
import org.springframework.http.HttpStatus;

import static com.tave.tavewebsite.domain.question.exception.ErrorMessage.QUESTION_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class QuestionNotFoundException extends BaseErrorException {
    public QuestionNotFoundException() {
        super(BAD_REQUEST.value(), QUESTION_NOT_FOUND.getMessage());
    }
}
