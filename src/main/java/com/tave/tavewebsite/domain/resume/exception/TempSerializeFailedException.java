package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.TEMP_SERIALIZE_FAILED;

public class TempSerializeFailedException extends BaseErrorException {
    public TempSerializeFailedException() {
        super(TEMP_SERIALIZE_FAILED.getCode(), TEMP_SERIALIZE_FAILED.getMessage());
    }
}
