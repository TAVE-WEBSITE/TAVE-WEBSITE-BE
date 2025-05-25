package com.tave.tavewebsite.domain.resume.entity;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeEvaluateReqDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_evaluation_id")
    private Long id;

    @Column
    private double evaluationScore;

    @Column
    private String evaluationOpinion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvaluationStatus finalEvaluateDocument;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvaluationStatus finalEvaluateInterview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Builder
    public ResumeEvaluation(ResumeEvaluateReqDto resumeEvaluateReqDto, Member member, Resume resume) {
        this.evaluationScore = resumeEvaluateReqDto.score();
        this.evaluationOpinion = resumeEvaluateReqDto.opinion();
        this.member = member;
        this.resume = resume;
        this.finalEvaluateDocument = EvaluationStatus.COMPLETE;
        this.finalEvaluateInterview = EvaluationStatus.NOTCHECKED;
    }

    public static ResumeEvaluation of(ResumeEvaluateReqDto resumeEvaluateReqDto, Member member, Resume resume) {
        return new ResumeEvaluation(resumeEvaluateReqDto, member, resume);
    }
}
