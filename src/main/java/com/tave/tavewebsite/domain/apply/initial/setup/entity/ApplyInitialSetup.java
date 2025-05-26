package com.tave.tavewebsite.domain.apply.initial.setup.entity;

import com.tave.tavewebsite.domain.apply.initial.setup.dto.request.ApplyInitialSetupRequestDto;
import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplyInitialSetup extends BaseEntity {

    @Id
    private Long id = 1L;

    @NotNull
    @Size(min = 1, max = 5)
    @Column(length = 5, nullable = false)
    private String generation;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime documentRecruitStartDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime documentRecruitEndDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime documentAnnouncementDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime interviewStartDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime interviewEndDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime lastAnnouncementDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime accessStartDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime accessEndDate;

    @Builder
    public ApplyInitialSetup(String generation, LocalDateTime documentRecruitStartDate,
                             LocalDateTime documentAnnouncementDate, LocalDateTime documentRecruitEndDate,
                             LocalDateTime interviewEndDate, LocalDateTime accessStartDate,
                             LocalDateTime lastAnnouncementDate, LocalDateTime accessEndDate,
                             LocalDateTime interviewStartDate) {
        this.generation = generation;
        this.documentRecruitStartDate = documentRecruitStartDate;
        this.documentRecruitEndDate = documentRecruitEndDate;
        this.documentAnnouncementDate = documentAnnouncementDate;
        this.interviewStartDate = interviewStartDate;
        this.interviewEndDate = interviewEndDate;
        this.lastAnnouncementDate = lastAnnouncementDate;
        this.accessStartDate = accessStartDate;
        this.accessEndDate = accessEndDate;
    }

    public void updateFrom(ApplyInitialSetupRequestDto dto) {
        this.generation = dto.generation();
        this.documentRecruitStartDate = dto.documentRecruitStartDate();
        this.documentRecruitEndDate = dto.documentRecruitEndDate();
        this.documentAnnouncementDate = dto.documentAnnouncementDate();
        this.interviewStartDate = dto.interviewStartDate();
        this.interviewEndDate = dto.interviewEndDate();
        this.lastAnnouncementDate = dto.lastAnnouncementDate();
        this.accessStartDate = dto.accessStartDate();
        this.accessEndDate = dto.accessEndDate();
    }

}
