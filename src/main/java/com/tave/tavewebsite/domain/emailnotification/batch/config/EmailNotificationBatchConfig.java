package com.tave.tavewebsite.domain.emailnotification.batch.config;

import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotification;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class EmailNotificationBatchConfig extends DefaultBatchConfiguration {

    private final JpaPagingItemReader<EmailNotification> emailReader;
    private final ItemWriter<EmailNotification> emailWriter;

    @Bean
    public Job emailNotificationJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("emailNotificationJob", jobRepository)
                .start(emailNotificationStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step emailNotificationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("emailNotificationStep", jobRepository)
                .<EmailNotification, EmailNotification>chunk(50, transactionManager)
                .reader(emailReader)
                .writer(emailWriter)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);        // 동시에 실행할 쓰레드 수
        executor.setMaxPoolSize(8);         // 최대 허용 수
        executor.setQueueCapacity(100);     // 큐 사이즈
        executor.setThreadNamePrefix("batch-thread-");
        executor.initialize();
        return executor;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
