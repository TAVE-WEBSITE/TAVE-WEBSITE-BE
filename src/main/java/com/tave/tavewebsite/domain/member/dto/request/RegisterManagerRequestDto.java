package com.tave.tavewebsite.domain.member.dto.request;

import com.tave.tavewebsite.domain.member.entity.DepartmentType;
import com.tave.tavewebsite.domain.member.entity.JobType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterManagerRequestDto(

        @Email(message = "이메일 형식에 맞게 작성해주세요.")
        String email,

        @NotNull(message = "비밀번호를 입력하세요.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).+$",
                message = "비밀번호는 8자 이상이어야 하며, 대문자, 소문자, 특수문자를 포함해야 합니다.")
        String password,
        String nickname,
        String username,
        String agitId,
        String generation,
        DepartmentType department,
        JobType job

) {
}
