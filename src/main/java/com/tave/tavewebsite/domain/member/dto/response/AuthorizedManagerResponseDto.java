package com.tave.tavewebsite.domain.member.dto.response;

import com.tave.tavewebsite.domain.member.entity.DepartmentType;
import com.tave.tavewebsite.domain.member.entity.JobType;
import com.tave.tavewebsite.domain.member.entity.Member;

public record AuthorizedManagerResponseDto(
        String username,
        DepartmentType department,
        JobType job,
        String generation,
        String agitId
) {
    public static AuthorizedManagerResponseDto fromEntity(Member member) {
        return new AuthorizedManagerResponseDto(
                member.getUsername(),
                member.getDepartment(),
                member.getJob(),
                member.getGeneration(),
                member.getAgitId()
        );
    }
}
