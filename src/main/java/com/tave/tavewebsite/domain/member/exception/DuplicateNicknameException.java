package com.tave.tavewebsite.domain.member.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.member.exception.ErrorMessage.DUPLICATE_NICKNAME;

public class DuplicateNicknameException extends BaseErrorException {
    public DuplicateNicknameException() {
        super(DUPLICATE_NICKNAME.getCode(), DUPLICATE_NICKNAME.getMessage());
    }
}
