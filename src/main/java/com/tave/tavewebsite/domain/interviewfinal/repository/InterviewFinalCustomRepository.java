package com.tave.tavewebsite.domain.interviewfinal.repository;

import com.tave.tavewebsite.domain.interviewfinal.dto.response.InterviewFinalResDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.global.common.FieldType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewFinalCustomRepository {

    Page<InterviewFinalResDto> findInterviewFinalEvaluation(Pageable pageable, EvaluationStatus status, FieldType type);
}
