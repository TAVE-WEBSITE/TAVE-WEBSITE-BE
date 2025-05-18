package com.tave.tavewebsite.domain.member.exception;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage.UNAUTHORIZED_MANAGER;

import com.tave.tavewebsite.global.exception.BaseErrorException;

public class UnauthorizedMemberException extends BaseErrorException {
    public UnauthorizedMemberException() {
        super(UNAUTHORIZED_MANAGER.getCode(), UNAUTHORIZED_MANAGER.getMessage());
    }
}
