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
            super(JwtErrorMessage.NEED_ACCESS_TOKEN_REFRESH.getErrorCode(),
                    JwtErrorMessage.NEED_ACCESS_TOKEN_REFRESH.getMessage());
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

    public static class NotMatchRefreshTokenException extends BaseErrorException {
        public NotMatchRefreshTokenException() {
            super(JwtErrorMessage.NOT_MATCH_REFRESH_TOKEN.getErrorCode(),
                    JwtErrorMessage.NOT_MATCH_REFRESH_TOKEN.getMessage());
        }
    }

    public static class SignOutUserException extends BaseErrorException {
        public SignOutUserException() {
            super(JwtErrorMessage.SIGN_OUT_USER.getErrorCode(),
                    JwtErrorMessage.SIGN_OUT_USER.getMessage());
        }
    }

    public static class CannotUseRefreshToken extends BaseErrorException {
        public CannotUseRefreshToken() {
            super(JwtErrorMessage.CANNOT_USE_REFRESH_TOKEN.getErrorCode(),
                    JwtErrorMessage.CANNOT_USE_REFRESH_TOKEN.getMessage());
        }
    }
}
