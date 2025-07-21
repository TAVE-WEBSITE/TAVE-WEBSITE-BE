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
        String univ,
        String birthday,
        String major,
        String minor
) {
    public static ResumeMemberInfoDto from(Resume resume, Member member) {
        return ResumeMemberInfoDto.builder()
                .username(member.getUsername())
                .sex(member.getSex().getDisplayName())
                .email(member.getEmail())
                .field(resume.getField().getDisplayName())
                .univ(resume.getSchool())
                .birthday(member.getBirthday().toString())
                .major(resume.getMajor())
                .minor(!resume.isDoubleMajor() ? resume.getMinor() : "")
                .build();
    }

    public static ResumeMemberInfoDto of(Resume resume) {
        return ResumeMemberInfoDto.builder()
                .username(resume.getMember().getUsername())
                .sex(resume.getMember().getSex().getDisplayName())
                .email(resume.getMember().getEmail())
                .field(resume.getField().getDisplayName())
                .univ(resume.getSchool())
                .birthday(resume.getMember().getBirthday().toString())
                .major(resume.getMajor())
                .minor(!resume.isDoubleMajor() ? resume.getMinor() : "")
                .build();
    }
}
