package com.tave.tavewebsite.domain.apply.initial.setup.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ApplyInitialSetupRequestDto(
        @NotNull(message = "generation 필수로 입력해주시기 바랍니다.") @Size(min = 1, max = 5, message = "1~5 글자 사이로 입력해주세요.")
        @Pattern(regexp = "^[0-9]+$", message = "generation은 숫자만 입력 가능합니다.")
        String generation,
        @NotNull(message = "documentRecruitStartDate 필수로 입력해주시기 바랍니다.")
        LocalDateTime documentRecruitStartDate,
        @NotNull(message = "documentRecruitEndDate 필수로 입력해주시기 바랍니다.")
        LocalDateTime documentRecruitEndDate,
        @NotNull(message = "documentAnnouncementDate 필수로 입력해주시기 바랍니다.")
        LocalDateTime documentAnnouncementDate,
        @NotNull(message = "interviewStartDate 필수로 입력해주시기 바랍니다.")
        LocalDateTime interviewStartDate,
        @NotNull(message = "interviewEndDate 필수로 입력해주시기 바랍니다.")
        LocalDateTime interviewEndDate,
        @NotNull(message = "lastAnnouncementDate 필수로 입력해주시기 바랍니다.")
        LocalDateTime lastAnnouncementDate,
        @NotNull(message = "accessStartDate 필수로 입력해주시기 바랍니다.")
        LocalDateTime accessStartDate,
        @NotNull(message = "accessEndDate 필수로 입력해주시기 바랍니다.")
        LocalDateTime accessEndDate
) {
}
