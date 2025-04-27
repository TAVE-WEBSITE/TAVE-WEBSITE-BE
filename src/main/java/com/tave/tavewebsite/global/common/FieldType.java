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
    COLLABORATIVE("Collaborative");   // 연합

    private final String message;
}
