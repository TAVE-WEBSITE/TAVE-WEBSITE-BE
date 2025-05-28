package com.tave.tavewebsite.domain.interviewplace.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

    INTERVIEW_PLACE_GET("면접 장소 정보를 조회합니다"),
    INTERVIEW_PLACE_CREATED("면접 장소 정보를 성공적으로 생성했습니다.");


    private final String message;
}
