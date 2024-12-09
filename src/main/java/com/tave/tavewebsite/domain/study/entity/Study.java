package com.tave.tavewebsite.domain.study.entity;

import com.tave.tavewebsite.domain.study.dto.StudyRequestDto;
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
public class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studyId")
    private Long id;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(length = 30, nullable = false)
    private String teamName; // 스터디 팀 이름

    @NotNull
    @Size(min = 1, max = 500)
    @Column(nullable = false)
    private String topic; // 스터디 주제

    @NotNull
    @Size(min = 1, max = 5)
    @Column(length = 5, nullable = false)
    private String generation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FieldType field;

    @NotNull
    @Column(length = 2083, nullable = false) // DDL varchar(2083)
    private String blogUrl;

    @NotNull
    @Column(length = 2083, nullable = false) // DDL varchar(2083)
    private String imgUrl;

    @Builder
    public Study(StudyRequestDto req, URL imageUrl) {
        this.topic = req.topic();
        this.teamName = req.teamName();
        this.generation = req.generation();
        this.field = FieldType.valueOf(req.field());
        this.blogUrl = req.blogUrl();
        this.imgUrl = imageUrl.toString();
    }

    // test를 위한 생성
    public Study(Long id, String teamName, String topic, String generation, FieldType field, String blogUrl, String imgUrl) {
        this.id = id;
        this.teamName = teamName;
        this.topic = topic;
        this.generation = generation;
        this.field = field;
        this.blogUrl = blogUrl;
        this.imgUrl = imgUrl;
    }

    public void updateStudy(StudyRequestDto req, URL imageUrl) {
        this.topic = req.topic();
        this.teamName = req.teamName();
        this.generation = req.generation();
        this.field = FieldType.valueOf(req.field());
        this.blogUrl = req.blogUrl();
        this.imgUrl = imageUrl.toString();
    }


}
