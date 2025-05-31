package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeResDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumeCustomRepository {

    Page<ResumeResDto> findAll(Member member, EvaluationStatus status, Pageable pageable);

    long findNotEvaluatedResume(Member member);

    long findEvaluatedResume(Member member);
}
