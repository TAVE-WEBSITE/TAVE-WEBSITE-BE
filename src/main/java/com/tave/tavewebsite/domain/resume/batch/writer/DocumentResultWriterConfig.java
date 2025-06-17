package com.tave.tavewebsite.domain.resume.batch.writer;

import com.tave.tavewebsite.domain.resume.batch.entity.DocumentResultDLQ;
import com.tave.tavewebsite.domain.resume.batch.repository.DocumentResultDLQRepository;
import com.tave.tavewebsite.domain.resume.entity.Resume;
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
public class DocumentResultWriterConfig {

    private final SESMailService sesMailService;
    private final DocumentResultDLQRepository documentResultDLQRepository;

    @Bean(name = "documentResultWriter")
    public ItemWriter<Resume> documentResultWriter() {
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
            List<DocumentResultDLQ> failedItems = new ArrayList<>();

            for (Resume item : items) {
                try {
                    retryTemplate.execute(context -> {
                        sesMailService.sendDocumentResultMail(item.getMember().getEmail(),
                                item.getMember().getUsername(), item.getResumeGeneration());
                        log.info("메일 전송 성공: {}", item.getMember().getEmail());
                        return null;
                    }, context -> {
                        Throwable lastError = context.getLastThrowable();

                        if (lastError == null) {
                            log.error("DLQ 처리 - {}: 원인 불명 오류", item.getMember().getEmail());
                        } else {
                            log.error("DLQ 처리 - {}: {}", item.getMember().getEmail(), lastError.getMessage());
                        }

                        failedItems.add(DocumentResultDLQ.of(item.getMember().getEmail(), lastError));
                        return null;
                    });
                } catch (Exception e) {
                    failedItems.add(DocumentResultDLQ.of(item.getMember().getEmail(), e));
                    log.error("Unexpected failure: {}", item.getMember().getEmail(), e);
                }
            }

            // 실패 건: 개별 필드 변경사항 포함하여 DLQ로 saveAll
            if (!failedItems.isEmpty()) {
                documentResultDLQRepository.saveAll(failedItems);
                documentResultDLQRepository.flush();
            }
        };
    }
}
