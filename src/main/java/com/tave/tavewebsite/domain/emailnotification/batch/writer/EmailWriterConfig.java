package com.tave.tavewebsite.domain.emailnotification.batch.writer;

import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotification;
import com.tave.tavewebsite.domain.emailnotification.entity.EmailStatus;
import com.tave.tavewebsite.domain.emailnotification.repository.EmailNotificationRepository;
import com.tave.tavewebsite.global.mail.service.MailService;
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

    private final MailService mailService;
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
            for (EmailNotification item : items) {
                try {
                    retryTemplate.execute(context -> {
                        mailService.sendManagerRegisterMessage(item.getEmail());
                        item.changeStatus(EmailStatus.SUCCESS);
                        log.info("메일 전송 성공: {}", item.getEmail());
                        return null;
                    }, context -> {
                        item.changeStatus(EmailStatus.FAILED);
                        log.error("DLQ 처리 - {}: {}", item.getEmail(), Objects.requireNonNull(context.getLastThrowable())
                                .getMessage());
                        return null;
                    });
                } catch (Exception e) {
                    item.changeStatus(EmailStatus.FAILED);
                    log.error("Unexpected failure: {}", item.getEmail(), e);
                }
                emailNotificationRepository.save(item);
            }
            emailNotificationRepository.flush();
        };
    }
}

//    @Bean
//    public ItemWriter<EmailNotification> emailWriter() {
//        return items -> {
//            for (EmailNotification item : items) {
//                try {
//                    mailService.sendManagerRegisterMessage(item.getEmail());
//                    item.changeStatus(EmailStatus.SUCCESS);
//                } catch (Exception e) {
//                    int retry = item.updateCounter();
//                    if (retry >= 3) {
//                        item.changeStatus(EmailStatus.FAILED);
//                    }
//                    log.error("이메일 전송 실패 - {}: {}", item.getEmail(), e.getMessage());
//                }
//            }
//            emailNotificationRepository.flush();
//        };
//    }


