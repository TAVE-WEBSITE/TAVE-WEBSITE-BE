package com.tave.tavewebsite.domain.resume.batch.repository;

import com.tave.tavewebsite.domain.resume.batch.entity.LastResultDLQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LastResultDLQRepository extends JpaRepository<LastResultDLQ, Long> {
}
