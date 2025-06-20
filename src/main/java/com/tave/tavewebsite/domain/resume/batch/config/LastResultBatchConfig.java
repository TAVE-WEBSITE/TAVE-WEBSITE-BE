package com.tave.tavewebsite.domain.resume.batch.config;

import com.tave.tavewebsite.domain.resume.batch.reader.ResumeIdRangePartitioner;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class LastResultBatchConfig extends DefaultBatchConfiguration {

    private final ItemWriter<Resume> lastResultWriter;
    private final ResumeRepository resumeRepository;
    private final JpaPagingItemReader<Resume> resumeReader;

    private final int threadSize = 3;

    public LastResultBatchConfig(
            @Qualifier("lastResultWriter") ItemWriter<Resume> lastResultWriter,
            ResumeRepository resumeRepository,
            JpaPagingItemReader<Resume> resumeReader
    ) {
        this.lastResultWriter = lastResultWriter;
        this.resumeRepository = resumeRepository;
        this.resumeReader = resumeReader;
    }

    @Bean
    public Job lastResultJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("lastResultJob", jobRepository)
                .start(partitionedLastResultStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step partitionedLastResultStep(JobRepository jobRepository,
                                          PlatformTransactionManager transactionManager) {
        return new StepBuilder("partitionedLastResultStep", jobRepository)
                .partitioner("lastResultStep", new ResumeIdRangePartitioner(resumeRepository))
                .gridSize(threadSize)
                .step(lastResultStep(jobRepository, transactionManager))
                .taskExecutor(taskExecutor(threadSize))
                .build();
    }

    @Bean
    public Step lastResultStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("lastResultStep", jobRepository)
                .<Resume, Resume>chunk(1000, transactionManager)
                .reader(resumeReader)
                .writer(lastResultWriter)
                .build();
    }

    public TaskExecutor taskExecutor(int threadPoolSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolSize);
        executor.setMaxPoolSize(threadPoolSize);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("last-result-batch-thread-");
        executor.initialize();
        return executor;
    }
}
