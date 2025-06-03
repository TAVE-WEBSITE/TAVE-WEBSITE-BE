package com.tave.tavewebsite.domain.emailnotification.batch.reader;

import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotification;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailReaderConfig {

    @Bean
    @StepScope // step scope로 런타임 시 동적으로 변수 값 할당
    public JpaPagingItemReader<EmailNotification> emailReader(
            @Value("#{stepExecutionContext['startId']}") Long startId,
            @Value("#{stepExecutionContext['endId']}") Long endId,
            EntityManagerFactory entityManagerFactory
    ) {
        JpaPagingItemReader<EmailNotification> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(
                "SELECT e FROM EmailNotification e WHERE e.status = 'PENDING' AND e.id BETWEEN :startId AND :endId");
        reader.setParameterValues(Map.of("startId", startId, "endId", endId));
        reader.setPageSize(1000);
        reader.setSaveState(false);
        reader.setName("emailReader");
        return reader;
    }
}
