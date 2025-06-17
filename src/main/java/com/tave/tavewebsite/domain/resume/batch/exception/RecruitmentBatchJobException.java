package com.tave.tavewebsite.domain.resume.batch.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

public abstract class RecruitmentBatchJobException {

    public static class DocumentResultBatchJobFailException extends BaseErrorException {
        public DocumentResultBatchJobFailException() {
            super(RecruitmentBatchErrorMessage.APPLY_EMAIL_BATCH_JOB_FAIL.getCode(),
                    RecruitmentBatchErrorMessage.APPLY_EMAIL_BATCH_JOB_FAIL.getMessage());
        }
    }

    public static class LastResultBatchJobFailException extends BaseErrorException {
        public LastResultBatchJobFailException() {
            super(RecruitmentBatchErrorMessage.LAST_EMAIL_BATCH_JOB_FAIL.getCode(),
                    RecruitmentBatchErrorMessage.LAST_EMAIL_BATCH_JOB_FAIL.getMessage());
        }
    }

}
