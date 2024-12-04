package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage._NOT_MATCHED_PASSWORD;

public class NotMatchedPassword extends BaseErrorException {
    public NotMatchedPassword() {
        super(_NOT_MATCHED_PASSWORD.getCode(), _NOT_MATCHED_PASSWORD.getMessage());
    }
}
