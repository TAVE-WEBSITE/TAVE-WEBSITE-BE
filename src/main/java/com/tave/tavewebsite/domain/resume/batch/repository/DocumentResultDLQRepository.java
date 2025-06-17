package com.tave.tavewebsite.domain.resume.batch.repository;

import com.tave.tavewebsite.domain.resume.batch.entity.DocumentResultDLQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentResultDLQRepository extends JpaRepository<DocumentResultDLQ, Long> {
}
