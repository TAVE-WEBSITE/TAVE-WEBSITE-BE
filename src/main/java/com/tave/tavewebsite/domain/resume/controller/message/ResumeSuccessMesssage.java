package com.tave.tavewebsite.domain.resume.controller.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResumeSuccessMesssage {

    RESUME_GET_TO_DATE_AND_TIME_SUCCESS("면접 [Date]와 [Time]으로 지원서 목록을 조회합니다.");

    private final String message;

    public String getMessage() {
        return message;
    }
}
