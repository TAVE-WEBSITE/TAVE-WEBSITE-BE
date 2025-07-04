package com.tave.tavewebsite.domain.resume.mapper;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoCreateRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.PersonalInfoResponseDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.global.common.FieldType;

public class ResumeMapper {

    public static Resume toResume(PersonalInfoCreateRequestDto requestDto, Member member, FieldType fieldType) {
        return Resume.builder()
                .member(member)
                .school(requestDto.getSchool())
                .major(requestDto.getMajor())
                .minor(requestDto.getMinor())
                .field(fieldType)
                .resumeGeneration(requestDto.getGeneration())
                .build();
    }

    public static PersonalInfoResponseDto toPersonalInfoResponseDto(Resume resume, Member member) {
        return PersonalInfoResponseDto.builder()
                .username(member.getUsername())
                .sex(member.getSex().name())
                .birthday(member.getBirthday().toString())
                .phoneNumber(member.getPhoneNumber())
                .email(member.getEmail())
                .school(resume.getSchool())
                .major(resume.getMajor())
                .minor(resume.getMinor())
                .field(resume.getField().getMessage())
                .build();
    }
}
