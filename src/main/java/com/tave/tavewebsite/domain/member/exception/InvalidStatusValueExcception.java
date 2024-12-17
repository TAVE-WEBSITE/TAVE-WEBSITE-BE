package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage.INVALID_STATUS_VALUE;

public class InvalidStatusValueExcception extends BaseErrorException {
    public InvalidStatusValueExcception() {
        super(INVALID_STATUS_VALUE.getCode(), INVALID_STATUS_VALUE.getMessage());
    }
}
