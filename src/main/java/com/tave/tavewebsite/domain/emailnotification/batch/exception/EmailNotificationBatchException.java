package com.tave.tavewebsite.domain.emailnotification.batch.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

public abstract class EmailNotificationBatchException {

    public static class ApplyEmailBatchJobFailException extends BaseErrorException {
        public ApplyEmailBatchJobFailException() {
            super(EmailNotificationBatchErrorMessage.APPLY_EMAIL_BATCH_JOB_FAIL.getCode(),
                    EmailNotificationBatchErrorMessage.APPLY_EMAIL_BATCH_JOB_FAIL.getMessage());
        }
    }

    public static class ApplyEmailFindException extends BaseErrorException {
        public ApplyEmailFindException() {
            super(EmailNotificationBatchErrorMessage.APPLY_EMAIL_NOT_FOUND.getCode(),
                    EmailNotificationBatchErrorMessage.APPLY_EMAIL_NOT_FOUND.getMessage());
        }
    }


}
