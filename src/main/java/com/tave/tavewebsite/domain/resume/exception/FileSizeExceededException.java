package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.FILE_SIZE_EXCEEDED;

public class FileSizeExceededException extends BaseErrorException {
    public FileSizeExceededException() {
        super(FILE_SIZE_EXCEEDED.getCode(), FILE_SIZE_EXCEEDED.getMessage());
    }
}
