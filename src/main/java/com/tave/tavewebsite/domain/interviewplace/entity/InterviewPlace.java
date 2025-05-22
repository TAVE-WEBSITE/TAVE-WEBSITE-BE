package com.tave.tavewebsite.domain.interviewplace.entity;

import com.tave.tavewebsite.domain.interviewplace.dto.request.InterviewPlaceSaveDto;
import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewPlace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String generalAddress;

    public String detailAddress;

    // HACK 면접 기간을 나흘로 하드 코딩
    @Column(columnDefinition = "TEXT")
    public String firstOpenChatLink;

    @Column(columnDefinition = "TEXT")
    public String secondOpenChatLink;

    @Column(columnDefinition = "TEXT")
    public String thirdOpenChatLink;

    @Column(columnDefinition = "TEXT")
    public String fourthOpenChatLink;

    @Column(columnDefinition = "TEXTX")
    public String firstDocumentLink;

    @Column(columnDefinition = "TEXTX")
    public String secondDocumentLink;

    @Column(columnDefinition = "TEXTX")
    public String thridDocumentLink;

    @Column(columnDefinition = "TEXTX")
    public String fourthDocumentLink;

    public static InterviewPlace of(InterviewPlaceSaveDto dto) {
        return InterviewPlace.builder()
                .generalAddress(dto.generalAddress())
                .detailAddress(dto.detailAddress())
                .firstOpenChatLink(dto.firstOpenChatLink())
                .secondOpenChatLink(dto.secondOpenChatLink())
                .thirdOpenChatLink(dto.thirdOpenChatLink())
                .fourthOpenChatLink(dto.fourthOpenChatLink())
                .firstDocumentLink(dto.firstDocumentLink())
                .secondDocumentLink(dto.secondDocumentLink())
                .thridDocumentLink(dto.thridDocumentLink())
                .fourthDocumentLink(dto.fourthDocumentLink())
                .build();
    }

}
