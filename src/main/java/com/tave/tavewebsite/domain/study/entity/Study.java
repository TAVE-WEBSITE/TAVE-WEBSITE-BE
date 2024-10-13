package com.tave.tavewebsite.domain.study.entity;


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
public class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studyId")
    private Long id;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(length = 30, nullable = false)
    private String title;

    @NotNull
    @Size(min = 1, max = 500)
    @Column(nullable = false)
    private String description; // 스터디 주제

    @NotNull
    @Size(min = 1, max = 5)
    @Column(length = 5, nullable = false)
    private String generation;

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
    @Column(length = 2083, nullable = false) // DDL varchar(2083)
    private String blogUrl;

    @NotNull
    @Column(length = 2083, nullable = false) // DDL varchar(2083)
    private String imgUrl;

    @Builder
    public Study(String title, String description, String generation, String field, LocalDateTime startDate, LocalDateTime endDate, String blogUrl, String imgUrl) {
        this.title = title;
        this.description = description;
        this.generation = generation;
        this.field = field;
        this.startDate = startDate;
        this.endDate = endDate;
        this.blogUrl = blogUrl;
        this.imgUrl = imgUrl;
    }
}
