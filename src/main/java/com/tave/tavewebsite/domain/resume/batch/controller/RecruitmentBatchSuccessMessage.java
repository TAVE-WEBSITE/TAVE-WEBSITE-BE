package com.tave.tavewebsite.domain.resume.batch.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RecruitmentBatchSuccessMessage {
    DOCUMENT_RESULT_BATCH_JOB_EXECUTE("서류 평가 완료 메일 발송 작업이 예약되었습니다."
            + " 작업은 초기 지원 설정 페이지에 저장된 시간을 바탕으로 실행됩니다.");

    private final String message;
}
