package com.tave.tavewebsite.domain.member.dto.response;

import com.tave.tavewebsite.domain.member.entity.DepartmentType;
import com.tave.tavewebsite.domain.member.entity.JobType;
import com.tave.tavewebsite.domain.member.entity.Member;

public record ManagerResponseDto(
        Long id,
        String username,
        DepartmentType department,
        JobType job,
        String nickname,
        String generation,
        String agitId
) {
    public static ManagerResponseDto fromEntity(Member member) {
        return new ManagerResponseDto(
                member.getId(),
                member.getUsername(),
                member.getDepartment(),
                member.getJob(),
                member.getNickname(),
                member.getGeneration(),
                member.getAgitId()
        );
    }
}
