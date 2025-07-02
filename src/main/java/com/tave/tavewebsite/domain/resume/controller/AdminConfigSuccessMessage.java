package com.tave.tavewebsite.domain.resume.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminConfigSuccessMessage {

    GET_ALL_INTERVIEW_DATE_TIME("모든 면접일정 날짜와 시간 데이터를 반환합니다."),
    GET_DISTINCT_INTERVIEW_TIME_DAY("고유한 면접 날짜를 조회합니다."),
    CREATE_INTERVIEW_TIME_SUCCESS("면접 시간 설정에 성공했습니다.");

    private final String message;
}
