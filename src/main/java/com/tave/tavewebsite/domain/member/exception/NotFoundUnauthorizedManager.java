package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage.NOT_FOUND_UNAUTHORIZED_MANAGER;

public class NotFoundUnauthorizedManager extends BaseErrorException {
    public NotFoundUnauthorizedManager() {
        super(NOT_FOUND_UNAUTHORIZED_MANAGER.getCode(), NOT_FOUND_UNAUTHORIZED_MANAGER.getMessage());
    }
}
