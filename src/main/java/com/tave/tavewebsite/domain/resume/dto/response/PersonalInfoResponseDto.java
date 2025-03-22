package com.tave.tavewebsite.domain.resume.dto.response;

import com.tave.tavewebsite.domain.resume.entity.Resume;
import lombok.Getter;

@Getter
public class PersonalInfoResponseDto {
    private final String school;
    private final String major;
    private final String minor;
    private final String field;

    public PersonalInfoResponseDto(Resume resume) {
        this.school = resume.getSchool();
        this.major = resume.getMajor();
        this.minor = resume.getMinor();
        this.field = resume.getField();
    }
}
