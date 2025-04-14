package com.tave.tavewebsite.domain.resume.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TimeSlotSuccessMessage {

    UPLOAD_SUCCESS("면접 시간표 업로드에 성공하였습니다."),
    READ_SUCCESS("면접 시간표 조회에 성공하였습니다."),
    UPDATE_SUCCESS("면접 시간표 수정에 성공하였습니다.");

    private final String message;

    public String getMessage() {
        return message;
    }
}
