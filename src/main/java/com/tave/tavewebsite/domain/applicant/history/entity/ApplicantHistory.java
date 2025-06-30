package com.tave.tavewebsite.domain.applicant.history.entity;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.global.common.BaseEntity;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ApplicantHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 5)
    @Column(length = 5, nullable = false)
    private String generation;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public ApplicantHistory(String generation, FieldType fieldType, ApplicationStatus applicationStatus
            , Member member) {
        this.generation = generation;
        this.fieldType = fieldType;
        this.applicationStatus = applicationStatus;
        this.member = member;
        member.addApplicantHistory(this);
    }

    public void updateStatus(ApplicationStatus status) {
        this.applicationStatus = status;
    }
}
