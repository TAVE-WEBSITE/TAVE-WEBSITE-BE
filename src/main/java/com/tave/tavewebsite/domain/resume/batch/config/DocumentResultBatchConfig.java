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
public class DocumentResultBatchConfig extends DefaultBatchConfiguration {

    private final ItemWriter<Resume> documentResultWriter;
    private final ResumeRepository resumeRepository;
    private final JpaPagingItemReader<Resume> resumeReader;

    private final int threadSize = 3;

    public DocumentResultBatchConfig(
            @Qualifier("documentResultWriter") ItemWriter<Resume> documentResultWriter,
            ResumeRepository resumeRepository,
            JpaPagingItemReader<Resume> resumeReader
    ) {
        this.documentResultWriter = documentResultWriter;
        this.resumeRepository = resumeRepository;
        this.resumeReader = resumeReader;
    }

    @Bean
    public Job documentResultJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("documentResultJob", jobRepository)
                .start(partitionedDocumentResultStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step partitionedDocumentResultStep(JobRepository jobRepository,
                                              PlatformTransactionManager transactionManager) {
        return new StepBuilder("partitionedDocumentResultStep", jobRepository)
                .partitioner("documentResultStep", new ResumeIdRangePartitioner(resumeRepository))
                .gridSize(threadSize)
                .step(documentResultStep(jobRepository, transactionManager))
                .taskExecutor(taskExecutor(threadSize))
                .build();
    }

    @Bean
    public Step documentResultStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("documentResultStep", jobRepository)
                .<Resume, Resume>chunk(1000, transactionManager)
                .reader(resumeReader)
                .writer(documentResultWriter)
                .build();
    }

    public TaskExecutor taskExecutor(int threadPoolSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolSize);
        executor.setMaxPoolSize(threadPoolSize);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("document-batch-thread-");
        executor.initialize();
        return executor;
    }
}
