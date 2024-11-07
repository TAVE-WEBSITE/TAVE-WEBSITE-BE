package com.tave.tavewebsite.domain.member.service;

import com.tave.tavewebsite.domain.member.dto.request.RegisterManagerRequestDto;
import com.tave.tavewebsite.domain.member.dto.response.UnauthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.entity.RoleType;
import com.tave.tavewebsite.domain.member.exception.DuplicateEmailException;
import com.tave.tavewebsite.domain.member.exception.DuplicateNicknameException;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.global.mail.service.MailService;
import com.tave.tavewebsite.global.mail.dto.MailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tave.tavewebsite.domain.member.entity.RoleType.UNAUTHORIZED_MANAGER;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MailService mailService;

    @Transactional
    public MailResponseDto saveMember(RegisterManagerRequestDto requestDto){

        validateNickname(requestDto.nickname());
        validateEmail(requestDto.email());

        // PasswordEncoder 추가되면
        // toMember에서 encode(requestDto.getPassword()) 추가하기
        // 추가 후 위 주석은 삭제
        Member saveMember = memberRepository.save(Member.toMember(requestDto));
        return mailService.sendManagerRegisterMessage(saveMember.getEmail());
    }

    @Transactional(readOnly = true)
    public List<UnauthorizedManagerResponseDto>  getUnauthorizedManager(){
        return memberRepository.findByRole(UNAUTHORIZED_MANAGER).stream()
                .map(UnauthorizedManagerResponseDto::fromEntity)
                .toList();
    }

    private void validateEmail(String email){
        memberRepository.findByEmail(email).ifPresent(
                member -> {throw new DuplicateEmailException();}
        );
    }

    public void validateNickname(String nickname){
        memberRepository.findByNickname(nickname).ifPresent(
                member -> {throw new DuplicateNicknameException();}
        );
    }


}
