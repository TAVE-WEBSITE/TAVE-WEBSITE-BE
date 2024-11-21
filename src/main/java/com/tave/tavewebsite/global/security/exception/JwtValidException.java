package com.tave.tavewebsite.global.security.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

public abstract class JwtValidException {

    public static class InValidTokenException extends BaseErrorException {
        public InValidTokenException() {
            super(JwtErrorMessage.INVALID_JWT_TOKEN.getErrorCode(), JwtErrorMessage.INVALID_JWT_TOKEN.getMessage());
        }
    }

    public static class ExpiredTokenException extends BaseErrorException {
        public ExpiredTokenException() {
            super(JwtErrorMessage.EXPIRED_JWT_TOKEN.getErrorCode(), JwtErrorMessage.INVALID_JWT_TOKEN.getMessage());
        }
    }

    public static class UnsupportedTokenException extends BaseErrorException {
        public UnsupportedTokenException() {
            super(JwtErrorMessage.UNSUPPORTED_JWT_TOKEN.getErrorCode(),
                    JwtErrorMessage.UNSUPPORTED_JWT_TOKEN.getMessage());
        }
    }

    public static class EmptyClaimsException extends BaseErrorException {
        public EmptyClaimsException() {
            super(JwtErrorMessage.CLAIMS_IS_EMPTY.getErrorCode(), JwtErrorMessage.CLAIMS_IS_EMPTY.getMessage());
        }
    }
}
