package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.PORTFOLIO_OBJECT_NOT_FOUND;

public class PortfolioObjectNotFoundException extends BaseErrorException {
    public PortfolioObjectNotFoundException() {
        super(PORTFOLIO_OBJECT_NOT_FOUND.getCode(), PORTFOLIO_OBJECT_NOT_FOUND.getMessage());
    }
}
