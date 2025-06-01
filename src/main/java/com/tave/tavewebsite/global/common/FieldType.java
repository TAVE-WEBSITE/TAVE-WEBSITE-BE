package com.tave.tavewebsite.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldType {
    DESIGN("Design", "디자인"),
    DEEPLEARNING("Deep Learning", "딥러닝"),
    DATAANALYSIS("Data Analysis", "데이터분석"),
    FRONTEND("Frontend", "프론트엔드"),
    WEBFRONTEND("Web Frontend", "Web 프론트엔드"),
    APPFRONTEND("App Frontend", "App 프론트엔드"),
    BACKEND("Backend", "백엔드"),
    ADVANCED("Advanced", "심화"),       // 심화
    COMMON("Common", "공통"),
    COLLABORATIVE("Collaborative", "연합"),   // 연합

    EXCEL_PARSING_NULL("Parsing Error", "분석 에러"),
    PARSING_NULL("분석 실패", "분석 실패");

    private final String message;
    private final String displayName;
}
