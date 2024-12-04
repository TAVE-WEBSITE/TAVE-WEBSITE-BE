package com.tave.tavewebsite.domain.study.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    _NOT_FOUND_STUDY(400, "해당 스터디를 찾을 수 없습니다.");

    private final int code;
    private final String message;
}
