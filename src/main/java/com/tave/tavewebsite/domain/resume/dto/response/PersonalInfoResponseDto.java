package com.tave.tavewebsite.domain.resume.dto.response;

import lombok.*;

@ToString
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
    @Builder.Default
    private String school = "";
    @Builder.Default
    private String major = "";
    @Builder.Default
    private String minor = "";
    @Builder.Default
    private String field = "";
}
