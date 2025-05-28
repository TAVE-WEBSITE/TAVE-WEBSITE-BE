package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.resume.entity.ResumeEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeEvaluationRepository extends JpaRepository<ResumeEvaluation, Long> {

    boolean existsByMemberIdAndResumeId(Long memberId, Long resumeId);
}
