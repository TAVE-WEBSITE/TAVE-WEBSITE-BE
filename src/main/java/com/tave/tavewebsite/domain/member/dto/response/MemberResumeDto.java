package com.tave.tavewebsite.domain.member.dto.response;

public record MemberResumeDto(
        Long memberId,
        Long resumeId,
        String email
) {
}
