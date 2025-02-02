package com.tave.tavewebsite.domain.project.dto.response;

import com.tave.tavewebsite.domain.project.entity.Project;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.Getter;

@Getter
public class ProjectResponseDto {
    private final Long id;
    private final String title;
    private final String description;
    private final String generation;
    private final FieldType field;
    private final String blogUrl;

    public ProjectResponseDto(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.generation = project.getGeneration();
        this.field = project.getField();
        this.blogUrl = project.getBlogUrl();
    }
}
