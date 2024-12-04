package com.tave.tavewebsite.global.security.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

public abstract class LoginFailException {

    public static class EmailNotFoundException extends BaseErrorException {
        public EmailNotFoundException() {
            super(LoginErrorMessage.EMAIL_NOT_FOUND.getErrorCode(), LoginErrorMessage.EMAIL_NOT_FOUND.getMessage());
        }
    }

    public static class AuthenticationNotFoundException extends BaseErrorException {
        public AuthenticationNotFoundException() {
            super(LoginErrorMessage.AUTHENTICATION_NOT_FOUND.getErrorCode(),
                    LoginErrorMessage.AUTHENTICATION_NOT_FOUND.getMessage());
        }
    }
}
