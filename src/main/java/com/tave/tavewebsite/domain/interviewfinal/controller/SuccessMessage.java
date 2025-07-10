package com.tave.tavewebsite.domain.interviewfinal.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

    INTERVIEW_TIME_TABLE_FOR_MANAGER_SAVED(200, "면접에 참여하는 운영진이 볼 [면접 시간표]를 업로드했습니다"),
    INTERVIEW_FINAL_TIME_TABLE_LIST(200, "면접 시간표를 조회합니다."),
    INTERVIEW_FINAL_MEMBER_INFO(200, "회원의 면접 시간 및 장소 정보를 제공합니다."),
    INTERVIEW_FINAL_CREATED(200, "최종면접 데이터를 엑셀로부터 추출해 성공적으로 DB에 저장했습니다."),
    INTERVIEW_FINAL_LIST_GET(200, "최종 면접 페이지네이션 데이터를 반환합니다"),

    // 베타 테스트 용 기능
    BETA_INTERVIEW_FINAL_LIST_CREATED(200, "[BETA] 최종 면접 데이터를 DB에 저장했습니다.");


    private final int code;
    private final String message;
}
