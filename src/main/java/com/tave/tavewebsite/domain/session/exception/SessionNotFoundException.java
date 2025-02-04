package com.tave.tavewebsite.domain.session.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.session.exception.ErrorMessage.SESSION_NOT_FOUND;

public class SessionNotFoundException extends BaseErrorException {
    public SessionNotFoundException() {
        super(SESSION_NOT_FOUND.getCode(), SESSION_NOT_FOUND.getMessage());
    }
}
