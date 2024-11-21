package com.tave.tavewebsite.domain.member.service;

import static com.tave.tavewebsite.domain.member.entity.RoleType.UNAUTHORIZED_MANAGER;

import com.tave.tavewebsite.domain.member.dto.request.RegisterManagerRequestDto;
import com.tave.tavewebsite.domain.member.dto.request.SignUpRequestDto;
import com.tave.tavewebsite.domain.member.dto.response.UnauthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.exception.DuplicateEmailException;
import com.tave.tavewebsite.domain.member.exception.DuplicateNicknameException;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.global.mail.dto.MailResponseDto;
import com.tave.tavewebsite.global.mail.service.MailService;
import com.tave.tavewebsite.global.security.entity.JwtToken;
import com.tave.tavewebsite.global.security.utils.JwtTokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public MailResponseDto saveMember(RegisterManagerRequestDto requestDto) {

        validateNickname(requestDto.nickname());
        validateEmail(requestDto.email());

        Member saveMember = memberRepository.save(Member.toMember(requestDto, passwordEncoder));
        return mailService.sendManagerRegisterMessage(saveMember.getEmail());
    }

    @Transactional(readOnly = true)
    public List<UnauthorizedManagerResponseDto> getUnauthorizedManager() {
        return memberRepository.findByRole(UNAUTHORIZED_MANAGER).stream()
                .map(UnauthorizedManagerResponseDto::fromEntity)
                .toList();
    }

    public JwtToken signIn(SignUpRequestDto requestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                requestDto.email(),
                requestDto.password());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }

    private void validateEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(
                member -> {
                    throw new DuplicateEmailException();
                }
        );
    }

    public void validateNickname(String nickname) {
        memberRepository.findByNickname(nickname).ifPresent(
                member -> {
                    throw new DuplicateNicknameException();
                }
        );
    }


}
