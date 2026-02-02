package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage.IS_NOT_MANAGER_AUTHORIZATION;

public class IsNotManagerAuthorizationException extends BaseErrorException {
    public IsNotManagerAuthorizationException() {
        super(IS_NOT_MANAGER_AUTHORIZATION.getCode(), IS_NOT_MANAGER_AUTHORIZATION.getMessage());
    }
}