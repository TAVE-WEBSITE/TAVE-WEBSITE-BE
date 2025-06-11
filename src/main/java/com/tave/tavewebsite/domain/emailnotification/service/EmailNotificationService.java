package com.tave.tavewebsite.domain.emailnotification.service;

import com.tave.tavewebsite.domain.emailnotification.batch.exception.EmailNotificationBatchException.ApplyEmailFindException;
import com.tave.tavewebsite.domain.emailnotification.dto.request.EmailNotificationRequeestDto;
import com.tave.tavewebsite.domain.emailnotification.dto.response.EmailNotificationApplyResponseDto;
import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotification;
import com.tave.tavewebsite.domain.emailnotification.entity.EmailStatus;
import com.tave.tavewebsite.domain.emailnotification.repository.EmailNotificationRepository;
import com.tave.tavewebsite.domain.emailnotification.util.EmailNotificationMapper;
import com.tave.tavewebsite.global.mail.service.SESMailService;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

    private final EmailNotificationRepository emailNotificationRepository;
    private final RedisUtil redisUtil;
    private final SESMailService sesMailService;

    public void save(EmailNotificationRequeestDto dto) {
        emailNotificationRepository.save(EmailNotificationMapper.map(dto));
    }

    public void setSchedulerOfApplyNotificationEmail() {
        LocalDate targetDate;
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(3, 0))) {
            targetDate = LocalDate.now(); // 오늘 03:00 예약
        } else {
            targetDate = LocalDate.now().plusDays(1); // 내일 03:00 예약
        }

        String key = "email_batch_" + targetDate.format(DateTimeFormatter.ISO_DATE);

        // redis에 설정한 flag를 바탕으로 해당하는 날짜의 새벽 3시에 이메일 대량 발송 실행 예정
        redisUtil.set(key, "SCHEDULED", 26 * 60); // 넉넉하게 26시간으로 설정
    }

    public void cancelTodayAndTomorrowApplyNotificationSchedule() {
        cancelNotificationSchedule(LocalDate.now());
        cancelNotificationSchedule(LocalDate.now().plusDays(1));
    }

    private void cancelNotificationSchedule(LocalDate date) {
        String key = "email_batch_" + date.format(DateTimeFormatter.ISO_DATE);
        if (redisUtil.hasKey(key)) {
            redisUtil.delete(key);
            log.info("예약 취소 완료: {}", key);
            return;
        }
        log.warn("예약 키가 존재하지 않음: {}", key);
    }


    public void sendApplyEmailIndividual(Long id) {
        EmailNotification emailNotification = emailNotificationRepository.findById(id)
                .orElseThrow(ApplyEmailFindException::new);
        sesMailService.sendApplyNotification(emailNotification.getEmail());
        emailNotification.changeStatus(EmailStatus.SUCCESS);
    }

    public Page<EmailNotificationApplyResponseDto> getEmailNotificationByPageble(Pageable pageable) {
        return emailNotificationRepository.findAll(pageable)
                .map(EmailNotificationMapper::mapToResponseDto);
    }

}