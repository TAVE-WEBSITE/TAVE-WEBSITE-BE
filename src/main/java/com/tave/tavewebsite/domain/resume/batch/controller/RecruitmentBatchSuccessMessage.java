package com.tave.tavewebsite.domain.resume.batch.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RecruitmentBatchSuccessMessage {
    DOCUMENT_RESULT_BATCH_JOB_EXECUTE("서류 평가 완료 메일 발송 작업이 예약되었습니다."
            + " 작업은 초기 지원 설정 페이지에 저장된 시간을 바탕으로 실행됩니다."),
    DOCUMENT_RESULT_BATCH_JOB_CANCEL("서류 평가 완료 메일 발송 작업이 취소되었습니다."),
    LAST_RESULT_BATCH_JOB_EXECUTE("최종 평가 완료 메일 발송 작업이 예약되었습니다."
            + " 작업은 초기 지원 설정 페이지에 저장된 시간을 바탕으로 실행됩니다."),
    LAST_RESULT_BATCH_JOB_CANCEL("최종 평가 완료 메일 발송 작업이 취소되었습니다"),
    GET_DOCUMENT_RESERVATION("서류 평가 완료 이메일 단체 전송 예약 조회에 성공했습니다."),
    GET_INTERVIEW_RESERVATION("면접 평가 완료 이메일 단체 전송 예약 조회에 성공했습니다.");


    private final String message;
}
