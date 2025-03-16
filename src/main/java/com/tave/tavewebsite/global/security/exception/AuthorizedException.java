package com.tave.tavewebsite.global.security.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.global.security.exception.ErrorType.NOT_AUTHORIZED;

public class AuthorizedException extends BaseErrorException {
    public AuthorizedException() {
        super(NOT_AUTHORIZED.getCode(), NOT_AUTHORIZED.getMessage());
    }
}
