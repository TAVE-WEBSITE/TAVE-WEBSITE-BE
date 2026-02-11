package com.tave.tavewebsite.domain.resume.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PersonalInfoRequestDto {

    @NotNull
    @Size(min = 1, max = 20)
    private String school;

    @NotNull
    @Size(max = 50)
    private String major;

    @Size(max = 50)
    private String minor;

    @NotNull
    private String field;

    public PersonalInfoRequestDto(String school, String major, String minor, String field) {
        this.school = school;
        this.major = major;
        this.minor = minor;
        this.field = field;
    }

    public static PersonalInfoRequestDto fromCreateRequest(PersonalInfoCreateRequestDto createDto) {
        return new PersonalInfoRequestDto(
                createDto.getSchool(),
                createDto.getMajor(),
                createDto.getMinor(),
                createDto.getField()
        );
    }
}
