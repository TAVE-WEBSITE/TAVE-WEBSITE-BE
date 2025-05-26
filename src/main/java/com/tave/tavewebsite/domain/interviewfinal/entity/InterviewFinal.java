package com.tave.tavewebsite.domain.interviewfinal.entity;

import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalConvertDto;
import com.tave.tavewebsite.domain.member.entity.Sex;
import com.tave.tavewebsite.global.common.BaseEntity;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewFinal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private Integer generation;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    private String university;

    private String interviewDay;

    private String interviewTime;

    private Long memberId;

    private Long resumeId;

}
