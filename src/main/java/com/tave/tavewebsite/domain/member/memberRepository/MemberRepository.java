package com.tave.tavewebsite.domain.member.memberRepository;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.entity.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String Nickname);

    Page<Member> findByRole(RoleType role, Pageable pageable);
}
