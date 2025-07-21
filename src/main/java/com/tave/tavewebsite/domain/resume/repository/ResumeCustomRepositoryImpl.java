package com.tave.tavewebsite.domain.resume.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeResDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.domain.resume.entity.QResumeEvaluation;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.tave.tavewebsite.domain.resume.entity.QResume.resume;
import static com.tave.tavewebsite.domain.resume.entity.QResumeEvaluation.resumeEvaluation;

@Repository
@RequiredArgsConstructor
public class ResumeCustomRepositoryImpl implements ResumeCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ResumeResDto> findMiddleEvaluation(Member member, EvaluationStatus status, FieldType type, Pageable pageable) {
        QResumeEvaluation resumeEvaluationSub = new QResumeEvaluation("reSub");

        BooleanBuilder condition = new BooleanBuilder();
        BooleanExpression statusCondition = extractedStatus(status);
        BooleanExpression typeCondition = extractedFieldType(type);

        if (statusCondition != null) {
            condition.and(statusCondition);
        }
        if (typeCondition != null) {
            condition.and(typeCondition);
        }

        List<ResumeResDto> resumeResDtos = queryFactory
                .select(Projections.constructor(
                        ResumeResDto.class,
                        resume.member.id,
                        resume.id,
                        resume.field,
                        resume.member.username,
                        resume.member.sex,
                        resume.school,
                        resume.createdAt,
                        JPAExpressions
                                .select(resumeEvaluationSub.count())
                                .from(resumeEvaluationSub)
                                .where(resumeEvaluationSub.resume.id.eq(resume.id)),
                        new CaseBuilder()
                                .when(resumeEvaluation.finalEvaluateDocument.isNull())
                                .then(EvaluationStatus.NOTCHECKED)
                                .otherwise(resumeEvaluation.finalEvaluateDocument)
                )).from(resume)
                .leftJoin(resumeEvaluation)
                .on(
                        resumeEvaluation.resume.id.eq(resume.id)
                        .and(resumeEvaluation.member.id.eq(member.getId()))
                )
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory
                        .select(resume.count())
                        .from(resume)
                        .leftJoin(resumeEvaluation)
                        .on(
                                resumeEvaluation.resume.id.eq(resume.id)
                                        .and(resumeEvaluation.member.id.eq(member.getId()))
                        )
                        .where(condition)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(resumeResDtos, pageable, total);
    }

    private BooleanExpression extractedStatus(EvaluationStatus status) {
        BooleanExpression condition;

        if(status == null)
            return null;

        if (status == EvaluationStatus.NOTCHECKED) {
            condition = resumeEvaluation.finalEvaluateDocument.isNull();
        } else {
            condition = resumeEvaluation.finalEvaluateDocument.eq(status);
        }

        return condition;
    }

    private BooleanExpression extractedFieldType(FieldType type) {
        BooleanExpression fieldType;

        if(type == null)
            return null;
        fieldType = resume.field.eq(type);

        return fieldType;
    }

    // 해당 조회는 EvaluationStatus 값이 평가 완료 or 평가 진행 전일 경우에 둘다 평가 진행 전으로 처리
    @Override
    public Page<ResumeResDto> findFinalEvaluation(Member member, EvaluationStatus status, FieldType type, Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();
        BooleanExpression statusCondition = extractedStatusInFinalEvaluation(status);
        BooleanExpression typeCondition = extractedFieldType(type);

        if (statusCondition != null) {
            condition.and(statusCondition);
        }
        if (typeCondition != null) {
            condition.and(typeCondition);
        }

        List<ResumeResDto> resumeResDtos = queryFactory
                .select(Projections.constructor(
                        ResumeResDto.class,
                        resume.member.id,
                        resume.id,
                        resume.field,
                        resume.member.username,
                        resume.member.sex,
                        resume.school,
                        Expressions.constant(LocalDateTime.now()),
                        resumeEvaluation.count(),
                        resume.finalDocumentEvaluationStatus
                )).from(resume)
                .leftJoin(resume.resumeEvaluations, resumeEvaluation)
                .groupBy(resume.id,
                        resume.field,
                        resume.member.username,
                        resume.member.sex,
                        resume.school,
                        resume.finalDocumentEvaluationStatus) //
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(resumeResDtos, pageable, resumeResDtos.size());
    }

    private BooleanExpression extractedStatusInFinalEvaluation(EvaluationStatus status) {
        BooleanExpression condition;

        if(status == null)
            return null;


        condition = resume.finalDocumentEvaluationStatus.eq(status);

        return condition;
    }

    @Override
    public long findNotEvaluatedResume(Member member) {
        Long resumeCount = queryFactory
                .select(resume.count())
                .from(resume)
                .fetchOne();

        Long evaluatedCount = queryFactory
                .select(resumeEvaluation.count())
                .from(resumeEvaluation)
                .where(resumeEvaluation.member.id.eq(member.getId()))
                .fetchOne();

        // null 방지 처리
        long safeResumeCount = resumeCount != null ? resumeCount : 0L;
        long safeEvaluatedCount = evaluatedCount != null ? evaluatedCount : 0L;

        return safeResumeCount - safeEvaluatedCount;
    }

    public long findEvaluatedResume(Member member) {
        Long result = queryFactory
                .select(resumeEvaluation.count())
                .from(resumeEvaluation)
                .where(resumeEvaluation.member.id.eq(member.getId()))
                .fetchOne();

        return Optional.ofNullable(result).orElse(0L); // null이면 0 반환
    }
}
