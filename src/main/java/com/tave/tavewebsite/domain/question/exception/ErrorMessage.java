package com.tave.tavewebsite.domain.question.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    QUESTION_NOT_FOUND("질문을 찾을 수 없습니다.");

    private final String message;
}
