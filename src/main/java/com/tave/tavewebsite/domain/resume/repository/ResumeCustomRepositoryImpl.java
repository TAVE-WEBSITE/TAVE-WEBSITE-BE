package com.tave.tavewebsite.domain.resume.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeResDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tave.tavewebsite.domain.resume.entity.QResume.resume;
import static com.tave.tavewebsite.domain.resume.entity.QResumeEvaluation.resumeEvaluation;

@Repository
@RequiredArgsConstructor
public class ResumeCustomRepositoryImpl implements ResumeCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ResumeResDto> findAll(Member member, EvaluationStatus status, Pageable pageable) {
        List<ResumeResDto> resumeResDtos = queryFactory
                .select(Projections.constructor(
                        ResumeResDto.class,
                        resume.id,
                        resume.field,
                        resume.member.username,
                        resume.member.sex,
                        resume.school,
                        resume.createdAt,
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
                .where(extractedStatus(status))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(resume.count())
                .from(resume)
                .fetchOne();

        return new PageImpl<>(resumeResDtos, pageable, total);
    }

    private BooleanExpression extractedStatus(EvaluationStatus status) {
        BooleanExpression condition;
        if (status == EvaluationStatus.NOTCHECKED) {
            condition = resumeEvaluation.finalEvaluateDocument.isNull();
        } else {
            condition = resumeEvaluation.finalEvaluateDocument.eq(status);
        }

        return condition;
    }

    @Override
    public long findNotEvaluatedResume(Member member) {
        Long resumeCount = queryFactory.select(resume.count()).from(resume).fetchOne();

        Long evaluatedCount = queryFactory
                .select(resumeEvaluation.count())
                .from(resumeEvaluation)
                .where(resumeEvaluation.member.id.eq(member.getId()))
                .fetchOne();

        return resumeCount - evaluatedCount;
    }

    @Override
    public long findEvaluatedResume(Member member) {
        return queryFactory
                .select(resumeEvaluation.count())
                .from(resumeEvaluation)
                .where(resumeEvaluation.member.id.eq(member.getId()))
                .fetchOne();
    }
}
