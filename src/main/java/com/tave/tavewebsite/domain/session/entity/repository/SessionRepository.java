package com.tave.tavewebsite.domain.session.entity.repository;


import com.tave.tavewebsite.domain.session.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

}
