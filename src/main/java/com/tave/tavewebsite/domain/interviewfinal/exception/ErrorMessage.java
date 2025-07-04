package com.tave.tavewebsite.domain.interviewfinal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    // InterviewFinal 오류
    NOT_FOUND_INTERVIEW_FINAL_BY_MEMBER(400,"해당 멤버의 면접 정보를 찾을 수 없습니다. "),

    // 엑셀 오류
    EXCEL_BAD_REQUEST_EXCEPTION(400, "엑셀 오류"),
    EXCEL_NULL_EXCEPTION(400,"현재 서버에서는 엑셀로부터 Null 값을 인식하고 있습니다.");

    private final int code;
    private final String message;
}
