package com.tave.tavewebsite.domain.apply.initial.setup.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ApplyInitialSetupReadResponseDto(
        String generation,
        LocalDateTime documentRecruitStartDate,
        LocalDateTime documentRecruitEndDate,
        LocalDateTime documentAnnouncementDate,
        LocalDateTime interviewStartDate,
        LocalDateTime interviewEndDate,
        LocalDateTime lastAnnouncementDate,
        LocalDateTime accessStartDate,
        LocalDateTime accessEndDate
) {
}
