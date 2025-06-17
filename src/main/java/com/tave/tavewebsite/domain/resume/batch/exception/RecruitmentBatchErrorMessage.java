package com.tave.tavewebsite.domain.resume.batch.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecruitmentBatchErrorMessage {

    APPLY_EMAIL_BATCH_JOB_FAIL(400, "서류 결과 발표 알림 메일 발송 batch 작업에 실패했습니다.");

    final int code;
    final String message;

}
