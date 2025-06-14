package com.tave.tavewebsite.domain.applicant.history.exception;

import com.tave.tavewebsite.domain.history.exception.HistoryErrorMessage;
import com.tave.tavewebsite.global.exception.BaseErrorException;

public abstract class ApplicantHistoryException {

    public static class ApplicantHistoryNotFoundException extends BaseErrorException {
        public ApplicantHistoryNotFoundException() {
            super(HistoryErrorMessage.NOT_FOUND_HISTORY.getCode(), HistoryErrorMessage.NOT_FOUND_HISTORY.getMessage());
        }
    }

}