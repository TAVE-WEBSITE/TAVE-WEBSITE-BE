package com.tave.tavewebsite.domain.interviewfinal.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.interviewfinal.exception.ErrorMessage.IS_NOT_XLSX_FILE_EXCEPTION;

public class IsNotXlsxFileException extends BaseErrorException {
    public IsNotXlsxFileException() {
        super(IS_NOT_XLSX_FILE_EXCEPTION.getCode(), IS_NOT_XLSX_FILE_EXCEPTION.getMessage());
    }
}
