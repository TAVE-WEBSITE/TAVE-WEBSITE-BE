package com.tave.tavewebsite.domain.member.service;

import com.tave.tavewebsite.domain.member.controller.MemberSuccessMessage;
import com.tave.tavewebsite.domain.member.dto.response.ManagerResponseDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.entity.RoleType;
import com.tave.tavewebsite.domain.member.exception.InvalidStatusValueExcception;
import com.tave.tavewebsite.domain.member.exception.NotFoundMemberException;
import com.tave.tavewebsite.domain.member.exception.NotManagerAccessException;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    public void updateAuthentication(String memberId) {
        Long id = Long.valueOf(memberId);
        Member member = memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
        member.updateRole();
    }

    public SuccessResponse<Page<ManagerResponseDto>> getManagersByStatus(String status, Pageable pageable) {
        Page<Member> members;
        String message;

        switch (status.toUpperCase()) {
            case "ALL":
                members = memberRepository.findAll(pageable);
                message = MemberSuccessMessage.ALL_MANAGER_READ.getMessage();
                break;
            case "AUTHORIZED":
                members = memberRepository.findByRole(RoleType.MANAGER, pageable);
                message = MemberSuccessMessage.AUTHORIZED_MEMBER_READ.getMessage();
                break;
            case "UNAUTHORIZED":
                members = memberRepository.findByRole(RoleType.UNAUTHORIZED_MANAGER, pageable);
                message = MemberSuccessMessage.UNAUTHORIZED_MEMBER_READ.getMessage();
                break;
            default:
                throw new InvalidStatusValueExcception();
        }

        Page<ManagerResponseDto> response = members.map(ManagerResponseDto::fromEntity);
        return new SuccessResponse<>(response, message);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
    }

    public void deleteManager(long memberId) {
        Member memberToDelete = findMemberById(memberId);

        // 일반 회원 및 다른 운영진이 탈퇴를 처리하지 못하도록 예외 처리
        if (!memberToDelete.getRole().equals(RoleType.MANAGER)) {
            throw new NotManagerAccessException();  // 운영진만 탈퇴 가능
        }

        memberRepository.deleteById(memberId);
    }

}
