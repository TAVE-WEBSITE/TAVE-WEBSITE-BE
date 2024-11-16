package com.tave.tavewebsite.domain.review.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SuccessMessage {

    REVIEW_CREATE("후기를 생성합니다."),
    REVIEW_GET_PUBLIC("공개 후기를 반환합니다."),
    REVIEW_GET_PRIVATE("비공개 후기를 반환합니다."),
    REVIEW_UPDATE("후기를 수정했습니다."),

    private final String message;

    public String getMessage(String generation) {
        return generation +" " + message;
    }

    public String getMessage() {
        return message;
    }
}
