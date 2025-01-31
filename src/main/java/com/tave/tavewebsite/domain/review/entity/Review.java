package com.tave.tavewebsite.domain.review.entity;

import com.tave.tavewebsite.domain.review.dto.request.ReviewRequestDto;
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

    private String companyName;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldType field;

    @NotNull
    @Column(columnDefinition = "VARCHAR(2000)", nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isPublic;

    @Builder
    public Review(String nickname, String generation, String companyName,FieldType field, String content, boolean isPublic) {
        this.nickname = nickname;
        this.generation = generation;
        this.companyName = companyName;
        this.field = field;
        this.content = content;
        this.isPublic = isPublic;
    }

    public void update(ReviewRequestDto requestDto) {
        this.nickname = requestDto.nickname();
        this.field = requestDto.field();
        this.generation = requestDto.generation();
        this.content = requestDto.content();
        this.isPublic = requestDto.isPublic();
    }
}
