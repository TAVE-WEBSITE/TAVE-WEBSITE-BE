package com.tave.tavewebsite.domain.interviewfinal.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.interviewfinal.exception.ErrorMessage.EXCEL_NULL_EXCEPTION;

public class ExcelNullPointException extends BaseErrorException {
    public ExcelNullPointException() {
        super(EXCEL_NULL_EXCEPTION.getCode(), EXCEL_NULL_EXCEPTION.getMessage());
    }
}
