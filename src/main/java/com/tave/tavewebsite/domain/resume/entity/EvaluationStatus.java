package com.tave.tavewebsite.domain.resume.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EvaluationStatus {

    FAIL("불합격"),
    PASS("합격"),
    HOLD("보류"),
    NOTCHECKED("평가 진행 전"),
    COMPLETE("평가 완료");

    private String status;
}
