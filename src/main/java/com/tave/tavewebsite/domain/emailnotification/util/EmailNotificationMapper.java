package com.tave.tavewebsite.domain.emailnotification.util;

import com.tave.tavewebsite.domain.emailnotification.dto.request.EmailNotificationRequeestDto;
import com.tave.tavewebsite.domain.emailnotification.dto.response.EmailNotificationApplyResponseDto;
import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotification;

public class EmailNotificationMapper {

    public static EmailNotification map(EmailNotificationRequeestDto emailNotificationRequeestDto) {
        return EmailNotification.builder()
                .email(emailNotificationRequeestDto.email())
                .build();
    }

    public static EmailNotificationApplyResponseDto mapToResponseDto(EmailNotification entity) {
        return new EmailNotificationApplyResponseDto(
                entity.getId(),
                entity.getEmail(),
                entity.getCreatedAt()
        );
    }
}
