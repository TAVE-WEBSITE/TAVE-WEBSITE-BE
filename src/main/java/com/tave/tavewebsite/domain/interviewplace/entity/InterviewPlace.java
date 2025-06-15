package com.tave.tavewebsite.domain.interviewplace.entity;

import com.tave.tavewebsite.domain.interviewplace.dto.request.InterviewPlaceSaveDto;
import com.tave.tavewebsite.global.common.BaseEntity;
import com.tave.tavewebsite.global.common.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class InterviewPlace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate interviewDate;

    private String generalAddress;

    private String detailAddress;

    @Column(columnDefinition = "TEXT")
    private String openChatLink;

    private String code;

    @Enumerated(EnumType.STRING)
    private Status status;

    public static InterviewPlace of(InterviewPlaceSaveDto dto) {
        return InterviewPlace.builder()
                .interviewDate(dto.interviewDate())
                .generalAddress(dto.generalAddress())
                .detailAddress(dto.detailAddress())
                .openChatLink(dto.openChatLink())
                .code(dto.code())
                .status(Status.ACTIVE)
                .build();
    }

    public void updateStatus(){
        this.status = Status.INACTIVE;
    }

}