package com.tave.tavewebsite.domain.project.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @Size(min = 1, max = 30)
    @Column(length = 30, nullable = false)
    private String field;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime endDate;

    @NotNull
    @Column(length = 2083, nullable = false)
    private String blogUrl;

    @NotNull
    @Column(length = 2083, nullable = false)
    private String imgUrl;

    @Builder
    public Project(String title, String description, String generation, String teamName, String field, LocalDateTime startDate, LocalDateTime endDate, String blogUrl, String imgUrl) {
        this.title = title;
        this.description = description;
        this.generation = generation;
        this.teamName = teamName;
        this.field = field;
        this.startDate = startDate;
        this.endDate = endDate;
        this.blogUrl = blogUrl;
        this.imgUrl = imgUrl;
    }
}
