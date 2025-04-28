package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.FIELD_TYPE_INVALID;

public class FieldTypeInvalidException extends BaseErrorException {
  public FieldTypeInvalidException() {
    super(FIELD_TYPE_INVALID.getCode(), FIELD_TYPE_INVALID.getMessage());
  }
}
