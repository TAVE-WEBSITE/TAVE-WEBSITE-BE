package com.tave.tavewebsite.domain.study.dto;


import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record StudyReq(

        @NotNull(message = "팀이름은 Null일 수 없습니다.")
        String teamName,

        @NotNull(message = "기수는 Null일 수 없습니다.")
        String generation,
        @NotNull(message = "분야는 Null일 수 없습니다.")
        String field,
        @NotNull(message = "스터디주제는 Null일 수 없습니다.")
        String topic,
        @NotNull(message = "블로그 url은 Null일 수 없습니다.")
        String blogUrl,

        MultipartFile imageFile
    ){}