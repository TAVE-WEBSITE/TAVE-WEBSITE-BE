package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage._EXPIRED_NUMBER;

public class ExpiredNumberException extends BaseErrorException {
    public ExpiredNumberException() {
        super(_EXPIRED_NUMBER.getCode(), _EXPIRED_NUMBER.getMessage());
    }
}
