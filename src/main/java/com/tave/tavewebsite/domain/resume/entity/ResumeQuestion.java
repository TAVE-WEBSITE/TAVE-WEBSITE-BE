package com.tave.tavewebsite.domain.resume.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ResumeQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

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

    @Builder
    public ResumeQuestion(Long questionId, String question, String answer, Integer ordered, Resume resume,
                          boolean specific) {
        this.questionId = questionId;
        this.question = question;
        this.answer = answer;
        this.ordered = ordered;
        this.resume = resume;

        // 분야별 질문이면 분야별 질문 리스트에 추가 아니면 공통질문에 추가
        if (specific) {
            resume.getSpecificResumeQuestion().add(this);
        } else {
            resume.getCommonResumeQuestion().add(this);
        }
    }
}
