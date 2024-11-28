package com.tave.tavewebsite.domain.review.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessgae {

    REVIEW_NOT_FOUND(400,"후기가 존재하지 않습니다.");

    private final int code;
    private final String message;
}
