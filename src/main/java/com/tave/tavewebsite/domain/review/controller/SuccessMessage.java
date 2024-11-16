package com.tave.tavewebsite.domain.review.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SuccessMessage {

    REVIEW_CREATE("후기를 생성합니다."),
    REVIEW_GET("후기를 반환합니다.");

    private final String message;

    public String getMessage(String generation) {
        return generation +" " + message;
    }
}
