package com.tave.tavewebsite.domain.emailnotification.batch.config;

import com.tave.tavewebsite.domain.emailnotification.batch.reader.IdRangePartitioner;
import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotification;
import com.tave.tavewebsite.domain.emailnotification.repository.EmailNotificationRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.Partitioner;
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

    private final ItemWriter<EmailNotification> emailWriter;
    private final EmailNotificationRepository emailNotificationRepository;
    private final JpaPagingItemReader<EmailNotification> emailReader;

    @Bean
    public Job emailNotificationJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("emailNotificationJob", jobRepository)
                .start(partitionedEmailStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step partitionedEmailStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("partitionedEmailStep", jobRepository)
                .partitioner("emailNotificationStep", idRangePartitioner())
                .step(emailNotificationStep(jobRepository, transactionManager))
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step emailNotificationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("emailNotificationStep", jobRepository)
                .<EmailNotification, EmailNotification>chunk(50, transactionManager)
                .reader(emailReader)
                .writer(emailWriter)
                .build();
    }

    @Bean
    public Partitioner idRangePartitioner() {
        return new IdRangePartitioner(emailNotificationRepository);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("batch-thread-");
        executor.initialize();
        return executor;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
