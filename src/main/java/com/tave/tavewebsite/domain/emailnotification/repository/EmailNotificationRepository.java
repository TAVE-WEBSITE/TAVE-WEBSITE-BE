package com.tave.tavewebsite.domain.emailnotification.repository;

import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
    @Query("SELECT MIN(e.id) FROM EmailNotification e WHERE e.status = 'PENDING'")
    Long findMinId();

    @Query("SELECT MAX(e.id) FROM EmailNotification e WHERE e.status = 'PENDING'")
    Long findMaxId();

}
