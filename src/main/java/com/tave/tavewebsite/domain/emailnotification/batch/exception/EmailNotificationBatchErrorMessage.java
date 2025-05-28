package com.tave.tavewebsite.domain.emailnotification.batch.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailNotificationBatchErrorMessage {

    APPLY_EMAIL_BATCH_JOB_FAIL(400, "apply email batch 작업에 실패했습니다."),
    APPLY_EMAIL_NOT_FOUND(400, "해당하는 Id의 이메일을 찾지 못하였습니다.");

    final int code;
    final String message;

}