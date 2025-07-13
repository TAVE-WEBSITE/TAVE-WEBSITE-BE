package com.tave.tavewebsite.domain.apply.dashboard.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.apply.dashboard.exception.DashboardErrorMessage.NOT_FOUND_DASHBOARD;

public class DashboardErrorException extends BaseErrorException {
    public DashboardErrorException() {
        super(NOT_FOUND_DASHBOARD.code, NOT_FOUND_DASHBOARD.getMessage());
    }
}
