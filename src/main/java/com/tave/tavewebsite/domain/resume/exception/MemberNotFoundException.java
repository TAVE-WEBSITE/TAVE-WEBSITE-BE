package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.MEMBER_NOT_FOUND;

public class MemberNotFoundException extends BaseErrorException {
  public MemberNotFoundException() {
    super(MEMBER_NOT_FOUND.getCode(), MEMBER_NOT_FOUND.getMessage());
  }
}
