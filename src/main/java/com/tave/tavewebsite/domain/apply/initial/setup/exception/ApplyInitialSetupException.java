package com.tave.tavewebsite.domain.apply.initial.setup.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

public abstract class ApplyInitialSetupException {

    public static class ApplyInitialSetupNotFoundException extends BaseErrorException {
        public ApplyInitialSetupNotFoundException() {
            super(ApplyInitialSetupErrorMessage.NOT_FOUND_APPLY_INITIAL_SETUP.getCode(),
                    ApplyInitialSetupErrorMessage.NOT_FOUND_APPLY_INITIAL_SETUP.getMessage());
        }
    }

}
