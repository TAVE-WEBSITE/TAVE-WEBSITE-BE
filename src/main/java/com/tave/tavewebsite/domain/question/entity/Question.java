package com.tave.tavewebsite.domain.question.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 300)
    @Column(length = 300, nullable = false)
    private String content;

    @NotNull
    @Column(nullable = false)
    private QuestionType questionType;

    @NotNull
    @Column(nullable = false)
    private Integer ordered;

    @Builder
    public Question(String content, QuestionType questionType, Integer ordered) {
        this.content = content;
        this.questionType = questionType;
        this.ordered = ordered;
    }
}
