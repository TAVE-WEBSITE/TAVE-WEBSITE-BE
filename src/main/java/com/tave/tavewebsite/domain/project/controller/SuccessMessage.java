package com.tave.tavewebsite.domain.project.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SuccessMessage {
    PROJECT_CREATE("프로젝트를 생성했습니다."),
    PROJECT_READ("프로젝트를 조회했습니다."),
    PROJECT_UPDATE("프로젝트를 수정했습니다."),
    PROJECT_DELETE("프로젝트를 삭제했습니다.");

    private final String message;

    public String getMessage(String generation) {
        return generation +" " + message;
    }

    public String getMessage() {
        return message;
    }
}
