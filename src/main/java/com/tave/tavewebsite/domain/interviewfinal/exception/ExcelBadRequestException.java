package com.tave.tavewebsite.domain.interviewfinal.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.interviewfinal.exception.ErrorMessage.EXCEL_BAD_REQUEST_EXCEPTION;

public class ExcelBadRequestException extends BaseErrorException {
    public ExcelBadRequestException() {
        super(EXCEL_BAD_REQUEST_EXCEPTION.getCode(), EXCEL_BAD_REQUEST_EXCEPTION.getMessage());
    }
}
