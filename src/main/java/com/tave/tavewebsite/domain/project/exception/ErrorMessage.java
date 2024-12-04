package com.tave.tavewebsite.domain.project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    PROJECT_NOT_FOUND(400, "프로젝트를 찾을 수 없습니다.");

    private final int code;
    private final String message;
}
