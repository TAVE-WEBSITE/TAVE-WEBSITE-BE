package com.tave.tavewebsite.domain.resume.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EvaluationStatus {

    FAIL("불합격"),
    PASS("합격"),
    FINAL_FAIL("최종불합격"), // 최종 면접 평가 불합격
    FINAL_PASS("최종합격"), // 최종 면접 평가 합격
    NOTCHECKED_IN_INTERVIEW("최종 합격 평가 진행 전"), // 서류 평가를 합격하여 면접 평가 상태에 접어듬.
    NOTCHECKED("평가 진행 전"),
    COMPLETE("평가 완료");

    private final String displayName;
}
