package com.tave.tavewebsite.domain.emailnotification.repository;

import com.tave.tavewebsite.domain.emailnotification.entity.EmailNotification;
import com.tave.tavewebsite.domain.emailnotification.entity.EmailStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
    @Query("SELECT MIN(e.id) FROM EmailNotification e WHERE e.status = 'PENDING'")
    Long findMinId();

    @Query("SELECT MAX(e.id) FROM EmailNotification e WHERE e.status = 'PENDING'")
    Long findMaxId();

    @Modifying(clearAutomatically = true)
    @Query("UPDATE EmailNotification e SET e.status = :status, e.retryCount = e.retryCount + 1, e.updatedAt = CURRENT_TIMESTAMP WHERE e.id IN :ids")
    void bulkUpdateStatus(@Param("status") EmailStatus status, @Param("ids") List<Long> ids);

    long countByStatus(EmailStatus status);
}
