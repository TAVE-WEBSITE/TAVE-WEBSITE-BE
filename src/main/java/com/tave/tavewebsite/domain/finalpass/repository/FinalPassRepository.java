package com.tave.tavewebsite.domain.finalpass.repository;

import com.tave.tavewebsite.domain.finalpass.entity.FinalPass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinalPassRepository extends JpaRepository<FinalPass, Long> {
    Optional<FinalPass> findTopBy();
}
