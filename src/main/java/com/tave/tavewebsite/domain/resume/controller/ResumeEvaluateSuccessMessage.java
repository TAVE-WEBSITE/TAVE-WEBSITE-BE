package com.tave.tavewebsite.domain.resume.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResumeEvaluateSuccessMessage {

    CREATE_SUCCESS("평가 작성에 성공했습니다."),
    CREATE_INTERVIEW_TIME_SUCCESS("면접 시간 설정에 성공했습니다."),
    UPDATE_SUCCESS("평가 수정에 성공했습니다."),
    READ_SUCCESS("평가 조회에 성공했습니다."),
    DELETE_SUCCESS("평가 삭제에 성공했습니다.");

    private final String message;

    public String getMessage() {
        return message;
    }
}
