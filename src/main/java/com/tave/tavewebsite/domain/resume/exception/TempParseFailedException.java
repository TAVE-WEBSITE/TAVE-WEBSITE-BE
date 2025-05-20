package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.TEMP_PARSE_FAILED;

public class TempParseFailedException extends BaseErrorException {
    public TempParseFailedException() {
        super(TEMP_PARSE_FAILED.getCode(), TEMP_PARSE_FAILED.getMessage());
    }
}
