package com.tave.tavewebsite.domain.emailnotification.batch.scheduler;

import com.tave.tavewebsite.domain.emailnotification.batch.exception.EmailNotificationBatchException.ApplyEmailBatchJobFailException;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class EmailBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job emailNotificationJob;
    private final RedisUtil redisUtil;

    public EmailBatchScheduler(
            JobLauncher jobLauncher,
            @Qualifier("emailNotificationJob") Job emailNotificationJob,
            RedisUtil redisUtil) {
        this.jobLauncher = jobLauncher;
        this.emailNotificationJob = emailNotificationJob;
        this.redisUtil = redisUtil;
    }


    @Scheduled(cron = "0 30 1 * * *") // 매일 새벽 0시
    @SchedulerLock(name = "emailNotificationJobLock", lockAtMostFor = "PT10M") // 락 10분간 유지
    public void executeIfScheduled() {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String key = "email_batch_" + today;

        if (checkReserved(key)) {
            return; // 예약된 작업이 없으면 실행 안 함
        }

        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(emailNotificationJob, params);

            log.info("신규 지원 이메일 배치 작업 실행 완료");
        } catch (Exception e) {
            log.error("신규 지원 이메일 배치 작업 실행 실패", e);
            throw new ApplyEmailBatchJobFailException();
        } finally {
            redisUtil.delete(key); // 중복 실행 방지
        }
    }

    private Boolean checkReserved(String key) {
        Boolean scheduled = redisUtil.hasKey(key);
        if (!Boolean.TRUE.equals(scheduled)) {
            return true; // 예약된 작업이 없으면 실행 안 함
        }
        return false;
    }
}

