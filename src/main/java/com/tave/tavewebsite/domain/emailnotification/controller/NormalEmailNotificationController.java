package com.tave.tavewebsite.domain.emailnotification.controller;

import com.tave.tavewebsite.domain.emailnotification.dto.request.EmailNotificationRequeestDto;
import com.tave.tavewebsite.domain.emailnotification.service.EmailNotificationService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/normal/notification")
@RequiredArgsConstructor
public class NormalEmailNotificationController {

    private final EmailNotificationService emailNotificationService;

    @PostMapping
    public SuccessResponse postEmailNotification(@RequestBody @Valid EmailNotificationRequeestDto dto) {
        emailNotificationService.save(dto);
        return SuccessResponse.ok(EmailNotificationSuccessMessage.POST_SUCCESS.getMessage());
    }
}
