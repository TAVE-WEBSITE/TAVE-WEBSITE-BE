package com.tave.tavewebsite.domain.interviewplace.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    NOT_FOUNT_INTERVIEW_PLACE(400,"아직 등록된 면접 장소 정보가 없습니다.");

    private final int code;
    private final String message;
}
