package com.tave.tavewebsite.domain.emailnotification.batch.writer;

import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotification;
import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotificationDLQ;
import com.tave.tavewebsite.domain.emailnotification.repository.EmailNotificationDLQRepository;
import com.tave.tavewebsite.domain.emailnotification.repository.EmailNotificationRepository;
import com.tave.tavewebsite.global.mail.service.SESMailService;
import java.util.ArrayList;
import java.util.List;
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
    private final EmailNotificationDLQRepository emailNotificationDLQRepository;

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
            List<Long> failedIds = new ArrayList<>();
            List<EmailNotificationDLQ> failedItems = new ArrayList<>();

            for (EmailNotification item : items) {
                try {
                    retryTemplate.execute(context -> {
                        sesMailService.sendApplyNotification(item.getEmail());
                        log.info("메일 전송 성공: {}", item.getEmail());
                        successIds.add(item.getId());
                        return null;
                    }, context -> {
                        Throwable lastError = context.getLastThrowable();

                        if (lastError == null) {
                            log.error("DLQ 처리 - {}: 원인 불명 오류", item.getEmail());
                        } else {
                            log.error("DLQ 처리 - {}: {}", item.getEmail(), lastError.getMessage());
                        }

                        failedItems.add(EmailNotificationDLQ.of(item, lastError));
                        failedIds.add(item.getId());
                        return null;
                    });
                } catch (Exception e) {
                    failedItems.add(EmailNotificationDLQ.of(item, e));
                    failedIds.add(item.getId());
                    log.error("Unexpected failure: {}", item.getEmail(), e);
                }
            }

            // 1. 성공 건: 전체 벌크 삭제
            if (!successIds.isEmpty()) {
                emailNotificationRepository.deleteAllByIdInBatch(successIds);
            }

            // 2. 실패 건: 기존 데이터베이스에서 삭제 및
            // 개별 필드 변경사항 포함하여 DLQ로 saveAll
            if (!failedItems.isEmpty()) {
                emailNotificationRepository.deleteAllByIdInBatch(failedIds);
                emailNotificationDLQRepository.saveAll(failedItems);
                emailNotificationDLQRepository.flush();
            }
        };
    }
}


