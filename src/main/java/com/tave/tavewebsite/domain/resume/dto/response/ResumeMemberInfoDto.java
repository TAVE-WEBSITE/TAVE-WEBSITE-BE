package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import lombok.Builder;

@Builder
public record ResumeMemberInfoDto(
        String username,
        String sex,
        String email,
        String field,
        String univ
) {
    public static ResumeMemberInfoDto from(Resume resume, Member member) {
        return ResumeMemberInfoDto.builder()
                .univ(resume.getSchool())
                .email(member.getEmail())
                .field(resume.getField().getDisplayName())
                .username(member.getUsername())
                .sex(member.getSex().getDisplayName())
                .build();
    }
}
