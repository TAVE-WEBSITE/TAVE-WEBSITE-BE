package com.tave.tavewebsite.domain.applicant.history.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApplicantHistorySuccessMessage {
    READ_SUCCESS("지원 이력 조회에 성공했습니다."),
    APPLICANT_STATUS_UPDATE_BY_DOCUMENT_SUCCESS("서류 평가 기반 지원 이력 상태 업데이트에 성공했습니다."),
    APPLICANT_STATUS_UPDATE_BY_INTERVIEW_SUCCESS("면접 평가 기반 지원 이력 상태 업데이트에 성공했습니다.");

    private final String message;

    public String getMessage() {
        return message;
    }
}
