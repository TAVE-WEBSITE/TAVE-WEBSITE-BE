package com.tave.tavewebsite.domain.study.dto;


public record StudyResponseDto(
        Long studyId,
        String teamName,
        String generation,
        String field,
        String topic,
        String imageUrl,
        String blogUrl
) {
}
