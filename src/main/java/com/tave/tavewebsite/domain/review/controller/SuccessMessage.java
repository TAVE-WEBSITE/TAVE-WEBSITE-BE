package com.tave.tavewebsite.domain.review.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

    REVIEW_CREATE("후기를 생성했습니다.");

    private final String message;
}
