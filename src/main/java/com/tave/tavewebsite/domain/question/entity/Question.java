package com.tave.tavewebsite.domain.question.entity;

import com.tave.tavewebsite.domain.question.dto.request.QuestionSaveRequest;
import com.tave.tavewebsite.domain.question.dto.request.QuestionUpdateRequest;
import com.tave.tavewebsite.global.common.BaseEntity;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
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
    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @NotNull
    @Column(nullable = false)
    private Integer ordered;

    private Integer textLength;

    public static Question from(QuestionSaveRequest dto) {
        return Question.builder()
                .content(dto.content())
                .fieldType(dto.fieldType())
                .ordered(dto.ordered())
                .textLength(dto.textLength())
                .build();
    }

    public void update(QuestionUpdateRequest dto) {
        this.content = dto.content();
        this.fieldType = dto.fieldType();
        this.ordered = dto.ordered();
        this.textLength = dto.textLength();
    }
}
