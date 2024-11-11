package com.tave.tavewebsite.domain.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyModifyDto {

    private String teamName;
    private String generation;
    private String field;
    private String topic;
    private String blogUrl;
    private String imageUrl;
}
