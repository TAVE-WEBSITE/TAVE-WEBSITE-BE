package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage.NOT_FOUNT_USER;

public class NotFoundMemberException extends BaseErrorException {
    public NotFoundMemberException() {
        super(NOT_FOUNT_USER.getCode(), NOT_FOUNT_USER.getMessage());
    }
}
