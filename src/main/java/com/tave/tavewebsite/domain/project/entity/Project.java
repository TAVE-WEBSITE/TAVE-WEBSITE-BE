package com.tave.tavewebsite.domain.project.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FieldType field;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime endDate;

    @NotNull
    @URL
    @Column(length = 2083, nullable = false)
    private String blogUrl;

    @NotNull
    @URL
    @Column(length = 2083, nullable = false)
    private String imgUrl;

    @Builder
    public Project(String title, String description, String generation, String teamName, FieldType field, LocalDateTime startDate, LocalDateTime endDate, String blogUrl, String imgUrl) {
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
