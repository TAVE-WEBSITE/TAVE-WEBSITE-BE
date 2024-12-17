package com.tave.tavewebsite.domain.session.repository;

import com.tave.tavewebsite.domain.session.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Page<Session> findByGenerationAndIsPublic(String generation, boolean isPublic, Pageable pageable);

}
