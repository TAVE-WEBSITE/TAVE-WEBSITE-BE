package com.tave.tavewebsite.domain.member.dto.request;

import com.tave.tavewebsite.domain.member.entity.DepartmentType;
import com.tave.tavewebsite.domain.member.entity.JobType;
import com.tave.tavewebsite.domain.member.entity.Sex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record RegisterMemberRequestDto(

        @NotNull(message ="이름을 입력해주세요")
        String username,

        @NotNull(message = "전화번호를 입력해주세요.")
        @Pattern(
                regexp = "^(\\d{3})-(\\d{4})-(\\d{4})$",
                message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)"
        )
        String phoneNumber,

        @NotNull(message = "생일을 입력해주세요.")
        @Pattern(
                regexp = "^(\\d{4})-(\\d{2})-(\\d{2})$",
                message = "생일 형식이 올바르지 않습니다. (예: 2000-11-04)"
        )
        String birthday,

        @NotNull(message = "성별을 선택해주세요.")
        Sex sex,

        @Email(message = "이메일 형식에 맞게 작성해주세요.")
        String email,

        @NotNull(message = "비밀번호를 입력하세요.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).+$",
                message = "비밀번호는 8자 이상이어야 하며, 대문자, 소문자, 특수문자를 포함해야 합니다.")
        String password

) {
    public LocalDate getBirthdayAsLocalDate() {
        return LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
