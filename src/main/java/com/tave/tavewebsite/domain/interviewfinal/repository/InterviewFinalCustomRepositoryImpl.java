package com.tave.tavewebsite.domain.interviewfinal.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.InterviewFinalResDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.tave.tavewebsite.domain.interviewfinal.entity.QInterviewFinal.interviewFinal;
import static com.tave.tavewebsite.domain.resume.entity.QResume.resume;

@Repository
@RequiredArgsConstructor
public class InterviewFinalCustomRepositoryImpl implements InterviewFinalCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<InterviewFinalResDto> findInterviewFinalEvaluation(Pageable pageable, EvaluationStatus status, FieldType type) {

        BooleanBuilder condition = new BooleanBuilder();
        BooleanExpression statusCondition = extractedStatus(status);
        BooleanExpression typeCondition = extractedFieldType(type);

        if (statusCondition != null) {
            condition.and(statusCondition);
        }
        if (typeCondition != null) {
            condition.and(typeCondition);
        }

        List<InterviewFinalResDto> interviewFinalResDtos = queryFactory
                .select(Projections.constructor(
                        InterviewFinalResDto.class,
                        interviewFinal.id,
                        interviewFinal.fieldType,
                        interviewFinal.username,
                        interviewFinal.sex,
                        interviewFinal.university,
                        interviewFinal.interviewDate,
                        resume.finalDocumentEvaluationStatus,
                        interviewFinal.memberId,
                        interviewFinal.resumeId
                )).from(interviewFinal)
                .leftJoin(resume)
                .on(
                    resume.id.eq(interviewFinal.resumeId)
                )
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory
                        .select(interviewFinal.count())
                        .from(interviewFinal)
                        .leftJoin(resume)
                        .on(
                                resume.id.eq(interviewFinal.resumeId)
                        )
                        .where(condition)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(interviewFinalResDtos, pageable, total);
    }

    private BooleanExpression extractedStatus(EvaluationStatus status) {

        if(status == null)
            return null;

        return resume.finalDocumentEvaluationStatus.eq(status);
    }

    private BooleanExpression extractedFieldType(FieldType type) {
        BooleanExpression fieldType;

        if(type == null)
            return null;
        fieldType = interviewFinal.fieldType.eq(type);

        return fieldType;
    }
}
