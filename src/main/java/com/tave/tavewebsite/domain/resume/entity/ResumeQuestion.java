package com.tave.tavewebsite.domain.resume.entity;

import com.tave.tavewebsite.domain.question.entity.Question;
import com.tave.tavewebsite.global.common.BaseEntity;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

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

    @Size(min = 1, max = 1000)
    @Column(length = 1000)
    private String answer;

    @Size(min = 1, max = 2)
    @Column(length = 2)
    private Integer ordered;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    // ResumeQuestion Builder
    public static ResumeQuestion of(Resume resume, Question question, FieldType fieldType) {

        // ResumeQuestion Builder
        ResumeQuestion resumeQuestion = ResumeQuestion.builder()
                .resume(resume)
                .question(question.getContent())
                .fieldType(fieldType)
                .ordered(question.getOrdered()) // ResumeQuestion의 order는 question을 따르는가? or 자체적인 0~5인가?
                .build();

        // Resume 측에도 resumeQuestion 설정
        resume.addCommonQuestion(resumeQuestion);

        return resumeQuestion;
    }
}
