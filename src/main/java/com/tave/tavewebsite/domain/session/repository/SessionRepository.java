package com.tave.tavewebsite.domain.session.repository;

import com.tave.tavewebsite.domain.session.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByGenerationAndIsPublic(String generation, boolean isPublic);
}
