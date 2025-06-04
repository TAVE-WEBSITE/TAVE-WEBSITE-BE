package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.resume.entity.ResumeTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeTimeSlotRepository extends JpaRepository<ResumeTimeSlot, Long> {

    List<ResumeTimeSlot> findAllByResumeId(Long resumeId);

    void deleteAllByResumeId(Long resumeId);
}
