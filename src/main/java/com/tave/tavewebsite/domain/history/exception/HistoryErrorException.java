package com.tave.tavewebsite.domain.history.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

public abstract class HistoryErrorException {

    public static class HistoryNotFoundException extends BaseErrorException {
        public HistoryNotFoundException() {
            super(HistoryErrorMessage.NOT_FOUND_HISTORY.getCode(), HistoryErrorMessage.NOT_FOUND_HISTORY.getMessage());
        }
    }

}