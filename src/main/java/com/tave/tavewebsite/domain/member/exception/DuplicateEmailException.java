package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage.DUPLICATE_EMAIL;

public class DuplicateEmailException extends BaseErrorException {
    public DuplicateEmailException() {
        super(DUPLICATE_EMAIL.getCode(), DUPLICATE_EMAIL.getMessage());
    }
}
