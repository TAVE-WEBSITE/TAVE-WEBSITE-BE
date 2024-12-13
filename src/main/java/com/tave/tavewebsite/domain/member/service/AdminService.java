package com.tave.tavewebsite.domain.member.service;

import static com.tave.tavewebsite.domain.member.entity.RoleType.MANAGER;
import static com.tave.tavewebsite.domain.member.entity.RoleType.UNAUTHORIZED_MANAGER;

import com.tave.tavewebsite.domain.member.dto.response.AuthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.UnauthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.entity.RoleType;
import com.tave.tavewebsite.domain.member.exception.NotFoundMemberException;
import com.tave.tavewebsite.domain.member.exception.NotManagerAccessException;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    @Transactional(readOnly = true)
    public List<AuthorizedManagerResponseDto> getAuthorizedAdmins() {
        return memberRepository.findByRole(MANAGER).stream()
                .map(AuthorizedManagerResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UnauthorizedManagerResponseDto> getUnauthorizedManager() {
        return memberRepository.findByRole(UNAUTHORIZED_MANAGER).stream()
                .map(UnauthorizedManagerResponseDto::fromEntity)
                .toList();
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
