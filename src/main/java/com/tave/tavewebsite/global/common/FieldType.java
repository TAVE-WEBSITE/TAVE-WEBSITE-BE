package com.tave.tavewebsite.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldType {
    DESIGN("Design"),
    DEEPLEARNING("Deep Learning"),
    DATAANALYSIS("Data Analysis"),
    FRONTEND("Frontend"),
    WEBFRONTEND("Web Frontend"),
    APPFRONTEND("App Frontend"),
    BACKEND("Backend"),
    ADVANCED("Advanced"),       // 심화
    COMMON("Common"),
    COLLABORATIVE("Collaborative"),   // 연합

    EXCEL_PARSING_NULL("Parsing Error"),
    PARSING_NULL("분석 실패");

    private final String message;
}
