package com.tave.tavewebsite.domain.resume.batch.reader;

import com.tave.tavewebsite.domain.resume.entity.Resume;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResumeReaderConfig {

    @Bean
    @StepScope // step scope로 런타임 시 동적으로 변수 값 할당
    public JpaPagingItemReader<Resume> resumeReader(
            @Value("#{stepExecutionContext['startId']}") Long startId,
            @Value("#{stepExecutionContext['endId']}") Long endId,
            EntityManagerFactory entityManagerFactory
    ) {
        JpaPagingItemReader<Resume> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(
                "SELECT r FROM Resume r WHERE r.id BETWEEN :startId AND :endId");
        reader.setParameterValues(Map.of("startId", startId, "endId", endId));
        reader.setPageSize(1000);
        reader.setSaveState(false);
        reader.setName("resumeReader");
        return reader;
    }
}
