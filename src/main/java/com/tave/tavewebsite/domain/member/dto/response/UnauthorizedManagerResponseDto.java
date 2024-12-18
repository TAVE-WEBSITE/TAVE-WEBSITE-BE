package com.tave.tavewebsite.domain.member.dto.response;

import com.tave.tavewebsite.domain.member.entity.DepartmentType;
import com.tave.tavewebsite.domain.member.entity.JobType;
import com.tave.tavewebsite.domain.member.entity.Member;

public record UnauthorizedManagerResponseDto(

        String username,
        DepartmentType department,
        JobType job,
        String generation,
        String agitId

) {
    public static UnauthorizedManagerResponseDto fromEntity(Member member) {
        return new UnauthorizedManagerResponseDto(
                member.getUsername(),
                member.getDepartment(),
                member.getJob(),
                member.getGeneration(),
                member.getAgitId()
        );
    }
}