package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage.NOT_FOUNT_USER;

public class NotFoundUserException extends BaseErrorException {
    public NotFoundUserException() {
        super(NOT_FOUNT_USER.getCode(), NOT_FOUNT_USER.getMessage());
    }
}
