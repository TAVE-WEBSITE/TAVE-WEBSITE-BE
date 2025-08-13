package com.tave.tavewebsite.domain.apply.dashboard.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tave.tavewebsite.domain.apply.dashboard.dto.DashboardUpdateDto;
import com.tave.tavewebsite.domain.member.entity.Sex;
import com.tave.tavewebsite.domain.resume.entity.ResumeState;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.tave.tavewebsite.domain.member.entity.QMember.member;
import static com.tave.tavewebsite.domain.resume.entity.QResume.resume;

@Repository
@RequiredArgsConstructor
public class DashboardCustomRepositoryImpl implements DashboardCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public DashboardUpdateDto countEachElements() {

        DashboardUpdateDto dashboardUpdateDto = queryFactory
                .select(
                        Projections.constructor(
                                DashboardUpdateDto.class,
                                resume.id.countDistinct().coalesce(0L),

                                // 성별
                                new CaseBuilder().when(member.sex.eq(Sex.MALE)).then(1L).otherwise(0L).sum().coalesce(0L),
                                new CaseBuilder().when(member.sex.eq(Sex.FEMALE)).then(1L).otherwise(0L).sum().coalesce(0L),

                                // 분야
                                new CaseBuilder().when(resume.field.eq(FieldType.BACKEND)).then(1L).otherwise(0L).sum().coalesce(0L),
                                new CaseBuilder().when(resume.field.eq(FieldType.WEBFRONTEND)).then(1L).otherwise(0L).sum().coalesce(0L),
                                new CaseBuilder().when(resume.field.eq(FieldType.DESIGN)).then(1L).otherwise(0L).sum().coalesce(0L),
                                new CaseBuilder().when(resume.field.eq(FieldType.APPFRONTEND)).then(1L).otherwise(0L).sum().coalesce(0L),
                                new CaseBuilder().when(resume.field.eq(FieldType.DATAANALYSIS)).then(1L).otherwise(0L).sum().coalesce(0L),
                                new CaseBuilder().when(resume.field.eq(FieldType.DEEPLEARNING)).then(1L).otherwise(0L).sum().coalesce(0L)
                        )).from(resume)
                .leftJoin(resume.member, member)
                .on(
                        member.id.eq(resume.member.id)
                )
                .where(resume.state.eq(ResumeState.SUBMITTED))
                .fetchOne();

        // null 반환 방지: 모두 0으로 채운 DTO
        return (dashboardUpdateDto != null)
                ? dashboardUpdateDto
                : new DashboardUpdateDto(0L,0L,0L,0L,0L,0L,0L,0L,0L);
    }
}
