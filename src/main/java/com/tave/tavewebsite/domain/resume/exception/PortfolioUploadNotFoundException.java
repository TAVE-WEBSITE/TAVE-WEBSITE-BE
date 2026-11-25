package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.PORTFOLIO_UPLOAD_NOT_FOUND;

public class PortfolioUploadNotFoundException extends BaseErrorException {
    public PortfolioUploadNotFoundException() {
        super(PORTFOLIO_UPLOAD_NOT_FOUND.getCode(), PORTFOLIO_UPLOAD_NOT_FOUND.getMessage());
    }
}
