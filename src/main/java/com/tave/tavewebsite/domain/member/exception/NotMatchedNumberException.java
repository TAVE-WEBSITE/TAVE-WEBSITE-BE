package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage._NOT_MATCHED_VERIFIED_NUMBER;

public class NotMatchedNumberException extends BaseErrorException {
    public NotMatchedNumberException() {
        super(_NOT_MATCHED_VERIFIED_NUMBER.getCode(), _NOT_MATCHED_VERIFIED_NUMBER.getMessage());
    }
}
