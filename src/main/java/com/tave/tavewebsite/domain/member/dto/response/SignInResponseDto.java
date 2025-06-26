package com.tave.tavewebsite.domain.member.dto.response;

import com.tave.tavewebsite.domain.member.entity.DepartmentType;
import com.tave.tavewebsite.domain.member.entity.JobType;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.entity.ResumeState;
import com.tave.tavewebsite.global.security.entity.JwtToken;

public record SignInResponseDto(
        String grantType,
        String accessToken,
        Long memberId,
        String email,
        String nickname,
        String username,
        String agitId,
        String generation,
        DepartmentType department,
        JobType job,
        ResumeState resumeState
) {
    public static SignInResponseDto from(JwtToken token, Member member, ResumeState resumeState) {
        return new SignInResponseDto(token.getGrantType(),
                token.getAccessToken(),
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getUsername(),
                member.getAgitId(),
                member.getGeneration(),
                member.getDepartment(),
                member.getJob(),
                resumeState);
    }
}
