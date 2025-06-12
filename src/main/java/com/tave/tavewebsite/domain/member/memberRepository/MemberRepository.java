package com.tave.tavewebsite.domain.member.memberRepository;

import com.tave.tavewebsite.domain.member.dto.response.MemberResumeDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.entity.RoleType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String Nickname);

    Page<Member> findByRole(RoleType role, Pageable pageable);

    @Query("""
        SELECT new com.tave.tavewebsite.domain.member.dto.response.MemberResumeDto(
            m.id,          \s
            r.id,          \s
            m.email        \s
        )
        FROM Resume r
        JOIN r.member m     \s
        WHERE r.resumeGeneration = :generation
          AND m.email IN :emails
        \s""")
    List<MemberResumeDto> findMemberIdAndResumeIdByGenAndEmails(
            @Param("generation") String generation,
            @Param("emails")     List<String> emails
    );
}
