package com.tave.tavewebsite.domain.emailnotification.repository;

import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotificationDLQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailNotificationDLQRepository extends JpaRepository<EmailNotificationDLQ, Long> {
}
