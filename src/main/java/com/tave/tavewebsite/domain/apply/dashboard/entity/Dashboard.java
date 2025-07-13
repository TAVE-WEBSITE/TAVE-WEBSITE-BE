package com.tave.tavewebsite.domain.apply.dashboard.entity;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.entity.Sex;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long totalCount = 0L;

    @Column(nullable = false)
    private Long previousCount = 0L;

    @Column(nullable = false)
    private Long comparisonRatio = 0L;

    @Column(nullable = false)
    private Long maleCount = 0L;

    @Column(nullable = false)
    private Long femaleCount = 0L;

    @Column(nullable = false)
    private Long backendCount = 0L;

    @Column(nullable = false)
    private Long webFrontCount = 0L;

    @Column(nullable = false)
    private Long designCount = 0L;

    @Column(nullable = false)
    private Long appFrontCount = 0L;

    @Column(nullable = false)
    private Long dataAnalysisCount = 0L;

    @Column(nullable = false)
    private Long deepCount = 0L;

    public Dashboard(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void initDashboard() {
        this.previousCount = totalCount;
        this.totalCount = 0L;
        this.comparisonRatio = 0L;
        this.maleCount = 0L;
        this.femaleCount = 0L;
        this.backendCount = 0L;
        this.appFrontCount = 0L;
        this.designCount = 0L;
        this.webFrontCount = 0L;
        this.dataAnalysisCount = 0L;
        this.deepCount = 0L;
    }

    public void updateDashboard(Resume resume, Member member) {
        this.totalCount += 1;
        if(member.getSex().equals(Sex.MALE)) {
            this.maleCount += 1;
        } else this.femaleCount += 1;

        if(resume.getField().equals(FieldType.APPFRONTEND)) {
            this.appFrontCount += 1;
        }
        else if(resume.getField().equals(FieldType.WEBFRONTEND)) {
            this.webFrontCount += 1;
        }
        else if(resume.getField().equals(FieldType.BACKEND)) {
            this.backendCount += 1;
        }
        else if(resume.getField().equals(FieldType.DESIGN)) {
            this.designCount += 1;
        }
        else if(resume.getField().equals(FieldType.DATAANALYSIS))
            this.dataAnalysisCount += 1;
        else if(resume.getField().equals(FieldType.DEEPLEARNING))
            this.deepCount += 1;

    }
}
