package com.tave.tavewebsite.domain.apply.initial.setup.util;

import com.tave.tavewebsite.domain.apply.initial.setup.dto.request.ApplyInitialSetupRequestDto;
import com.tave.tavewebsite.domain.apply.initial.setup.dto.response.ApplyInitialSetupReadResponseDto;
import com.tave.tavewebsite.domain.apply.initial.setup.entity.ApplyInitialSetup;

public class ApplyInitialSetUpMapper {

    public static ApplyInitialSetupReadResponseDto toApplyInitialSetupReadResponseDto(
            ApplyInitialSetup applyInitialSetup) {
        return ApplyInitialSetupReadResponseDto.builder()
                .generation(applyInitialSetup.getGeneration())
                .documentRecruitStartDate(applyInitialSetup.getDocumentRecruitStartDate())
                .documentRecruitEndDate(applyInitialSetup.getDocumentRecruitEndDate())
                .documentAnnouncementDate(applyInitialSetup.getDocumentAnnouncementDate())
                .interviewStartDate(applyInitialSetup.getInterviewStartDate())
                .interviewEndDate(applyInitialSetup.getInterviewEndDate())
                .lastAnnouncementDate(applyInitialSetup.getLastAnnouncementDate())
                .accessStartDate(applyInitialSetup.getAccessStartDate())
                .accessEndDate(applyInitialSetup.getAccessEndDate())
                .build();
    }

    public static ApplyInitialSetup toEntity(ApplyInitialSetupRequestDto dto) {
        return ApplyInitialSetup.builder()
                .generation(dto.generation())
                .documentRecruitStartDate(dto.documentRecruitStartDate())
                .documentRecruitEndDate(dto.documentRecruitEndDate())
                .documentAnnouncementDate(dto.documentAnnouncementDate())
                .interviewStartDate(dto.interviewStartDate())
                .interviewEndDate(dto.interviewEndDate())
                .lastAnnouncementDate(dto.lastAnnouncementDate())
                .accessStartDate(dto.accessStartDate())
                .accessEndDate(dto.accessEndDate())
                .build();
    }
}
