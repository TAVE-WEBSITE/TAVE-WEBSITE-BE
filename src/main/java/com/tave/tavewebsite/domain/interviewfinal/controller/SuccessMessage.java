package com.tave.tavewebsite.domain.interviewfinal.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

    INTERVIEW_FINAL_TIME_TABLE_LIST(200, "면접 시간표를 조회합니다."),
    INTERVIEW_FINAL_MEMBER_INFO(200, "회원의 면접 시간 및 장소 정보를 제공합니다."),
    INTERVIEW_FINAL_CREATED(200, "최종면접 데이터를 엑셀로부터 추출해 성공적으로 DB에 저장했습니다."),
    INTERVIEW_FINAL_LIST_GET(200, "최종 면접 페이지네이션 데이터를 반환합니다");

    private final int code;
    private final String message;
}
