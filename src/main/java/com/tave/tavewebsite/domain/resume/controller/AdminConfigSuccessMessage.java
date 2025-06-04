package com.tave.tavewebsite.domain.resume.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminConfigSuccessMessage {

    CREATE_INTERVIEW_TIME_SUCCESS("면접 시간 설정에 성공했습니다.");

    private final String message;
}
