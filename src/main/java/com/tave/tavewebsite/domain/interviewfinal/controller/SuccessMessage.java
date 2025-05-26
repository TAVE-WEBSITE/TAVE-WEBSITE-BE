package com.tave.tavewebsite.domain.interviewfinal.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

    INTERVIEW_FINAL_CREATED(200, "최종면접 데이터를 엑셀로부터 추출해 성공적으로 DB에 저장했습니다.");

    private final int code;
    private final String message;
}
