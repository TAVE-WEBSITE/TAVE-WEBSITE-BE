package com.tave.tavewebsite.domain.study.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StudySuccessMessage {

    STUDY_CREATE("스터디를 생성합니다."),
    STUDY_GET("스터디를 반환합니다."),
    STUDY_UPDATE("스터디를 수정했습니다."),
    STUDY_DELETE("스터디를 삭제했습니다.");

    private final String message;

    public String getMessage(String generation) {
        return generation +" " + message;
    }

    public String getMessage() {
        return message;
    }
}
