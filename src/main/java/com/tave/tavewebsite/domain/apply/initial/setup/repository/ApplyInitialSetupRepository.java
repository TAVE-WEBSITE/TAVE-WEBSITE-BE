package com.tave.tavewebsite.domain.apply.initial.setup.repository;

import com.tave.tavewebsite.domain.apply.initial.setup.entity.ApplyInitialSetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyInitialSetupRepository extends JpaRepository<ApplyInitialSetup, Long> {
}
