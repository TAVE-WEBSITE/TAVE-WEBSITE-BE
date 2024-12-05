package com.tave.tavewebsite.domain.member.dto.response;

import com.tave.tavewebsite.domain.member.entity.DepartmentType;
import com.tave.tavewebsite.domain.member.entity.JobType;
import com.tave.tavewebsite.domain.member.entity.Member;

public record AuthorizedManagerResponseDto(
        Long id,
        String username,
        String nickname,
        DepartmentType department,
        JobType job,
        String generation,
        String agitId
) {
    public static AuthorizedManagerResponseDto fromEntity(Member member) {
        return new AuthorizedManagerResponseDto(
                member.getId(),
                member.getUsername(),
                member.getNickname(),
                member.getDepartment(),
                member.getJob(),
                member.getGeneration(),
                member.getAgitId()
        );
    }
}
