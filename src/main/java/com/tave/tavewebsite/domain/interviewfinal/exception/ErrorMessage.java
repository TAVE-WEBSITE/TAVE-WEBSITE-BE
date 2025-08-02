package com.tave.tavewebsite.domain.interviewfinal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    // InterviewFinal 오류
    NOT_FOUND_INTERVIEW_FINAL_BY_MEMBER(400,"해당 멤버의 면접 정보를 찾을 수 없습니다. "),
    EMPTY_FILE_EXCEPTION(400, "요청에 파일이 존재하지 않습니다."),
    IS_NOT_XLSX_FILE_EXCEPTION(400, "해당 파일의 확장자가 XLSX가 아닙니다."),

    // 엑셀 오류
    EXCEL_BAD_REQUEST_EXCEPTION(400, "엑셀 오류"),
    EXCEL_NULL_EXCEPTION(400,"[Excel]에 비어있는 값이 존재합니다.");

    private final int code;
    private final String message;
}
