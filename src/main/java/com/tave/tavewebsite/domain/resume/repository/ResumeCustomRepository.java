package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeResDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.global.common.FieldType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumeCustomRepository {

    Page<ResumeResDto> findMiddleEvaluation(Member member, EvaluationStatus status, FieldType type, String name, Pageable pageable);

    Page<ResumeResDto> findFinalEvaluation(Member member, EvaluationStatus status, FieldType type, Pageable pageable);

    long findNotEvaluatedResume(Member member);

    long findEvaluatedResume(Member member);
}
