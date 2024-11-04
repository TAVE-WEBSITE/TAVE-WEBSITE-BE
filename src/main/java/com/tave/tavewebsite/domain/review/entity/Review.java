package com.tave.tavewebsite.domain.review.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewId")
    private Long id;

    private String nickname;

    @NotNull
    @Size(min = 1, max = 5)
    @Column(length = 5, nullable = false)
    private String generation;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldType field;

    @NotNull
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isPublic;

    @Builder
    public Review(String nickname, String generation, FieldType field, String content, boolean isPublic) {
        this.nickname = nickname;
        this.generation = generation;
        this.field = field;
        this.content = content;
        this.isPublic = isPublic;
    }
}