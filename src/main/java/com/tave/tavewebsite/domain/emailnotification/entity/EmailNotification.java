package com.tave.tavewebsite.domain.emailnotification.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailNotification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    private Integer retryCount;

    @Builder
    public EmailNotification(String email) {
        this.email = email;
        this.status = EmailStatus.PENDING;
        this.retryCount = 0;
    }

    public void changeStatus(EmailStatus status) {
        this.status = status;
    }

    public Integer updateCounter() {
        return this.retryCount++;
    }
}
