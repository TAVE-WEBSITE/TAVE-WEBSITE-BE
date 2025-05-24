package com.tave.tavewebsite.domain.resume.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoResponseDto {
    // 회원 정보
    private String username;
    private String sex;
    private String birthday;
    private String phoneNumber;
    private String email;

    // 이력서 정보
    private String school;
    private String major;
    private String minor;
    private String field;
}
