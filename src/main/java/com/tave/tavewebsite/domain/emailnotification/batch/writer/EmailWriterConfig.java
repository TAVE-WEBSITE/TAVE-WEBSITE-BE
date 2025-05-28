package com.tave.tavewebsite.domain.emailnotification.batch.writer;

import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotification;
import com.tave.tavewebsite.domain.emailnotification.entity.EmailStatus;
import com.tave.tavewebsite.domain.emailnotification.repository.EmailNotificationRepository;
import com.tave.tavewebsite.global.mail.service.SESMailService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class EmailWriterConfig {

    private final SESMailService sesMailService;
    private final EmailNotificationRepository emailNotificationRepository;

    @Bean
    public ItemWriter<EmailNotification> emailWriter() {
        RetryTemplate retryTemplate = new RetryTemplate();

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000); // 1초
        backOffPolicy.setMultiplier(2);         // 배수 증가
        backOffPolicy.setMaxInterval(10000);    // 최대 10초

        retryTemplate.setBackOffPolicy(backOffPolicy);

        // 재시도 횟수 설정
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        return items -> {
            List<Long> successIds = new ArrayList<>();
            List<EmailNotification> failedItems = new ArrayList<>();

            for (EmailNotification item : items) {
                try {
                    retryTemplate.execute(context -> {
                        sesMailService.sendApplyNotification(item.getEmail());
                        log.info("메일 전송 성공: {}", item.getEmail());
                        successIds.add(item.getId());
                        return null;
                    }, context -> {
                        item.changeStatus(EmailStatus.FAILED);
                        failedItems.add(item);
                        log.error("DLQ 처리 - {}: {}", item.getEmail(),
                                Objects.requireNonNull(context.getLastThrowable()).getMessage());
                        return null;
                    });
                } catch (Exception e) {
                    item.changeStatus(EmailStatus.FAILED);
                    failedItems.add(item);
                    log.error("Unexpected failure: {}", item.getEmail(), e);
                }
            }

            // 1. 성공 건: 벌크 업데이트
            if (!successIds.isEmpty()) {
                emailNotificationRepository.bulkUpdateStatus(EmailStatus.SUCCESS, successIds);
            }

            // 2. 실패 건: 개별 필드 변경사항 포함하여 saveAll
            if (!failedItems.isEmpty()) {
                emailNotificationRepository.saveAll(failedItems);
                emailNotificationRepository.flush();
            }
        };
    }
}


