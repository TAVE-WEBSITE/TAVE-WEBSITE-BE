package com.tave.tavewebsite.domain.session.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

    SESSION_SUCCESS_REGISTER("새로운 정규 세션 정보를 기입했습니다."),
    SESSION_SUCCESS_GET("성공적으로 세션을 조회했습니다."),
    SESSION_DELETE("성공적으로 세션을 삭제했습니다."),
    SESSION_UPDATE("성공적으로 세션을 수정했습니다.");

    private final String message;
}
