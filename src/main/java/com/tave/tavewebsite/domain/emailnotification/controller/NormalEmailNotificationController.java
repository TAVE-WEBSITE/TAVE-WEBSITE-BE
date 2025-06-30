package com.tave.tavewebsite.domain.emailnotification.controller;

import com.tave.tavewebsite.domain.emailnotification.dto.request.EmailNotificationRequeestDto;
import com.tave.tavewebsite.domain.emailnotification.dto.response.EmailNotificationApplyResponseDto;
import com.tave.tavewebsite.domain.emailnotification.service.EmailNotificationService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class NormalEmailNotificationController {

    private final EmailNotificationService emailNotificationService;

    @PostMapping("/normal/notification")
    public SuccessResponse postEmailNotification(@RequestBody @Valid EmailNotificationRequeestDto dto) {
        emailNotificationService.save(dto);
        return SuccessResponse.ok(EmailNotificationSuccessMessage.POST_SUCCESS.getMessage());
    }

    @GetMapping("/admin/notification/reservation")
    public SuccessResponse sendEmailBatch(HttpServletRequest request) {
        emailNotificationService.setSchedulerOfApplyNotificationEmail(request);
        return SuccessResponse.ok(EmailNotificationSuccessMessage.APPLY_EMAIL_BATCH_JOB_RESERVE.getMessage());
    }

    @DeleteMapping("/admin/notification/reservation")
    public SuccessResponse deleteEmailBatch() {
        emailNotificationService.cancelTodayAndTomorrowApplyNotificationSchedule();
        return SuccessResponse.ok(EmailNotificationSuccessMessage.DELETE_APPLY_EMAIL_BATCH_JOB_RESERVE.getMessage());
    }

    @GetMapping("/admin/notification/individual/{id}")
    public SuccessResponse sendEmailIndividual(@PathVariable("id") Long id) {
        emailNotificationService.sendApplyEmailIndividual(id);
        return SuccessResponse.ok(EmailNotificationSuccessMessage.APPLY_EMAIL_SEND_SUCCESS.getMessage());
    }

    @GetMapping("/admin/notification")
    public SuccessResponse<Page<EmailNotificationApplyResponseDto>> getEmailNotificationByPagination(
            @PageableDefault(size = 7, sort = "createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        Page<EmailNotificationApplyResponseDto> response = emailNotificationService.getEmailNotificationByPageble(
                pageable);
        return new SuccessResponse<>(response, EmailNotificationSuccessMessage.GET_SUCCESS.getMessage());
    }
}
