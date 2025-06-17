package com.tave.tavewebsite.domain.resume.batch.service;

import com.tave.tavewebsite.domain.resume.batch.exception.RecruitmentBatchJobException.DocumentResultBatchJobFailException;
import com.tave.tavewebsite.domain.resume.batch.exception.RecruitmentBatchJobException.LastResultBatchJobFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RecruitmentEmailBatchService {

    private final JobLauncher jobLauncher;
    private final Job documentResultJob;
    private final Job lastResultJob;

    public RecruitmentEmailBatchService(
            JobLauncher jobLauncher,
            @Qualifier("documentResultJob") Job documentResultJob,
            @Qualifier("lastResultJob") Job lastResultJob) {
        this.jobLauncher = jobLauncher;
        this.documentResultJob = documentResultJob;
        this.lastResultJob = lastResultJob;
    }

    @Async
    public void runDocumentResultJobAsync() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
            log.info("서류 모집 결과, 이메일 발송 배치 처리 시작");
            jobLauncher.run(documentResultJob, jobParameters);
            log.info("서류 모집 결과, 이메일 발송 배치 처리 완료");
        } catch (Exception e) {
            log.error("서류 모집 결과 이메일 발송 처리 실행 실패", e);
            throw new DocumentResultBatchJobFailException();
        }
    }

    @Async
    public void runLastResultJobAsync() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
            log.info("최종 결과 이메일 발송 배치 처리 시작");
            jobLauncher.run(lastResultJob, jobParameters);
            log.info("최종 결과 이메일 발송 배치 처리 완료");
        } catch (Exception e) {
            log.error("최종 결과 이메일 발송 처리 실행 실패", e);
            throw new LastResultBatchJobFailException();
        }
    }
}
