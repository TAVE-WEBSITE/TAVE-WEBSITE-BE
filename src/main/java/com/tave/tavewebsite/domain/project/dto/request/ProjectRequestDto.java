package com.tave.tavewebsite.domain.project.dto.request;

import com.tave.tavewebsite.global.common.FieldType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProjectRequestDto {

    @NotNull
    @Size(min = 1, max = 30)
    private String title;

    @NotNull
    @Size(min = 1, max = 500)
    private String description;

    @NotNull
    @Size(min = 1, max = 5)
    private String generation;

    @NotNull
    @Size(min = 1, max = 30)
    private String teamName;

    @NotNull
    private FieldType field;

    @NotNull
    private String blogUrl;

    @NotNull
    private String imgUrl;
}