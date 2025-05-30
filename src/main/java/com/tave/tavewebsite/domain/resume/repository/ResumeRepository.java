package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeCustomRepository {

    Optional<Resume> findByMember(Member member);

    @Query("select r from Resume r join fetch r.languageLevels join fetch r.member where r.id = :id")
    Resume findResumeWithLanguageLevels(@Param("id") Long id);

    Optional<Resume> findByMemberId(Long memberId);

    int countByHasChecked(Boolean hasChecked);

    long count();
}
