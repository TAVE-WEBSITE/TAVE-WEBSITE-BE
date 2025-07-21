package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.PDF_FILE_TYPE;

public class PdfFileTypeException extends BaseErrorException {
    public PdfFileTypeException() {
        super(PDF_FILE_TYPE.getCode(), PDF_FILE_TYPE.getMessage());
    }
}
