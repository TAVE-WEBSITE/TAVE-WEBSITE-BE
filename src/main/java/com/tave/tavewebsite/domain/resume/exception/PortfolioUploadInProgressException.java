package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.PORTFOLIO_UPLOAD_IN_PROGRESS;

public class PortfolioUploadInProgressException extends BaseErrorException {
    public PortfolioUploadInProgressException() {
        super(PORTFOLIO_UPLOAD_IN_PROGRESS.getCode(), PORTFOLIO_UPLOAD_IN_PROGRESS.getMessage());
    }
}
