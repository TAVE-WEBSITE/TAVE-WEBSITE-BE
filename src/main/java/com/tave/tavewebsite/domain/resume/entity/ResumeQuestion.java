package com.tave.tavewebsite.domain.resume.entity;

import com.tave.tavewebsite.domain.question.entity.Question;
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
public class ResumeQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // todo questionId가 필요한 지 의논
    // 어차피 질문 내용이 모집 중에 수정될 일 X -> 굳이 수정이 반영되도록 해야하는가?
//    private Long questionId;

    @NotNull
    @Size(min = 1, max = 300)
    @Column(length = 300, nullable = false)
    private String question;

    @Column(length = 1000)
    private String answer;

    @Column(length = 2)
    private Integer ordered;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    private Integer textLength;

    @NotNull
    @Column(nullable = false)
    private Boolean required;

    public static ResumeQuestion of(Resume resume, Question question) {

        return ResumeQuestion.builder()
                .resume(resume)
                .question(question.getContent())
                .fieldType(question.getFieldType())
                .ordered(question.getOrdered()) // todo Question 순서 사용 or 자체 ResumeQuestion 순서 생성? 의논
                .answerType(question.getAnswerType())
                .textLength(question.getTextLength() != null ? question.getTextLength() : 0)
                .required(question.getRequired())
                .build();
    }

    public void updateAnswer(String answer) {
        this.answer = answer;
    }
}
