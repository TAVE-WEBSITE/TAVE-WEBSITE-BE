package com.tave.tavewebsite.domain.emailnotification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailNotificationDLQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Lob  // TEXT 필드로 저장
    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime failedAt;

    public static EmailNotificationDLQ from(EmailNotification source, Throwable e) {
        EmailNotificationDLQ dlq = new EmailNotificationDLQ();
        dlq.email = source.getEmail();
        dlq.errorMessage = stackTraceToString(e);
        dlq.failedAt = LocalDateTime.now();
        return dlq;
    }

    private static String stackTraceToString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}

