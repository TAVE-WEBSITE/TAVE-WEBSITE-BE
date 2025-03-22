package com.tave.tavewebsite.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldType {
    DEEPLEARNING("Deep Learning"),
    DATAANALYSIS("Data Analysis"),
    FRONTEND("Frontend"),
    BACKEND("Backend"),
    ADVANCED("Advanced"),       // 심화
    COLLABORATIVE("Collaborative");   // 연합

    private final String message;
}
