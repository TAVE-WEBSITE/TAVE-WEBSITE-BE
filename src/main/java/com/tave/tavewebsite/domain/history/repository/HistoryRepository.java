package com.tave.tavewebsite.domain.history.repository;

import com.tave.tavewebsite.domain.history.entity.History;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByIsPublicOrderByGenerationDesc(Boolean isPublic);

    List<History> findAllByOrderByGenerationDesc();
}
