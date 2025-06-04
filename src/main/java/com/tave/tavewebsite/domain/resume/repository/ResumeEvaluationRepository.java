package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.domain.resume.entity.ResumeEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeEvaluationRepository extends JpaRepository<ResumeEvaluation, Long> {

    boolean existsByMemberIdAndResumeId(Long memberId, Long resumeId);

    ResumeEvaluation findByMemberIdAndResumeId(Long memberId, Long resumeId);

    List<ResumeEvaluation> findByResumeIdAndFinalEvaluateDocument(Long resumeId, EvaluationStatus status);
}
