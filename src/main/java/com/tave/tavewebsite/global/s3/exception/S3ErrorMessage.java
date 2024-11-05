package com.tave.tavewebsite.global.s3.exception;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum S3ErrorMessage {

    UPLOAD_FAIL(BAD_REQUEST, "S400", "사진 업로드에 실패하였습니다."),
    NOT_EXIST_NAME(BAD_REQUEST, "S401", "존재하지 않는 파일 이름입니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    S3ErrorMessage(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

}
