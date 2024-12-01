package com.tave.tavewebsite.domain.project.entity;

import com.tave.tavewebsite.domain.project.dto.request.ProjectRequestDto;
import com.tave.tavewebsite.global.common.BaseEntity;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectId")
    private Long id;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(length = 30, nullable = false)
    private String title;

    @NotNull
    @Size(min = 1, max = 500)
    @Column(length = 500, nullable = false)
    private String description;

    @NotNull
    @Size(min = 1, max = 5)
    @Column(length = 5, nullable = false)
    private String generation;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(length = 30, nullable = false)
    private String teamName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FieldType field;

    @NotNull
    @Column(length = 2083, nullable = false)
    private String blogUrl;

    @NotNull
    @Column(length = 2083, nullable = false)
    private String imgUrl;

    @Builder
    public Project(ProjectRequestDto req, URL imageUrl) {
        this.title = req.getTitle();
        this.description = req.getDescription();
        this.generation = req.getGeneration();
        this.teamName = req.getTeamName();
        this.field = req.getField();
        this.blogUrl = req.getBlogUrl();
        this.imgUrl = imageUrl.toString();
    }

    public Project updateProject(ProjectRequestDto req, URL imageUrl) {
        this.title = req.getTitle();
        this.description = req.getDescription();
        this.generation = req.getGeneration();
        this.teamName = req.getTeamName();
        this.field = req.getField();
        this.blogUrl = req.getBlogUrl();
        this.imgUrl = imageUrl.toString();
        return this;
    }
}
