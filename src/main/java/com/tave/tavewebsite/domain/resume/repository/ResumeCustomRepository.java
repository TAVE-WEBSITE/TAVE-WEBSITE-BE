package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumeCustomRepository {

    Page<ResumeResDto> findAll(Member member, Pageable pageable);
}
