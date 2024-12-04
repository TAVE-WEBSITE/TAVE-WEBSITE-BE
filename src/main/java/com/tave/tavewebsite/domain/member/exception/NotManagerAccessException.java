package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage.NOT_MANAGER;

public class NotManagerAccessException extends BaseErrorException {
    public NotManagerAccessException() {
        super(NOT_MANAGER.getCode(), NOT_MANAGER.getMessage());

    }
}
