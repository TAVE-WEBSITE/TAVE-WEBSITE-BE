package com.tave.tavewebsite.domain.resume.mapper;

import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.PersonalInfoResponseDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.member.entity.Member;

public class ResumeMapper {

    public static Resume toResume(PersonalInfoRequestDto requestDto, Member member) {
        return Resume.builder()
                .member(member)
                .school(requestDto.getSchool())
                .major(requestDto.getMajor())
                .minor(requestDto.getMinor())
                .field(requestDto.getField())
                .build();
    }

    public static PersonalInfoResponseDto toPersonalInfoResponseDto(Resume resume) {
        return new PersonalInfoResponseDto(
                resume.getSchool(),
                resume.getMajor(),
                resume.getMinor(),
                resume.getField()
        );
    }
}
