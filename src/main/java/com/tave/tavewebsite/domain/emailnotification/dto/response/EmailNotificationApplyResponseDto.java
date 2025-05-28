package com.tave.tavewebsite.domain.emailnotification.dto.response;

import java.time.LocalDateTime;

public record EmailNotificationApplyResponseDto(
        Long id,
        String email,
        LocalDateTime date
) {
}
