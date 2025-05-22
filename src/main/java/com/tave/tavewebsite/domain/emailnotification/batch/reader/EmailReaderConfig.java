package com.tave.tavewebsite.domain.emailnotification.batch.reader;

import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotification;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailReaderConfig {

    @Bean
    public JpaPagingItemReader<EmailNotification> emailReader(EntityManagerFactory entityManagerFactory) {
        JpaPagingItemReader<EmailNotification> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT e FROM EmailNotification e WHERE e.status = 'PENDING'");
        reader.setPageSize(50);
        reader.setSaveState(false);
        reader.setName("emailReader");
        return reader;
    }
}
