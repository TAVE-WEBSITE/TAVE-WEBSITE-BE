package com.tave.tavewebsite.domain.member.service;

import com.tave.tavewebsite.domain.member.dto.request.*;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.exception.DuplicateEmailException;
import com.tave.tavewebsite.domain.member.exception.DuplicateNicknameException;
import com.tave.tavewebsite.domain.member.exception.ExpiredNumberException;
import com.tave.tavewebsite.domain.member.exception.NotFoundMemberException;
import com.tave.tavewebsite.domain.member.exception.NotMatchedNumberException;
import com.tave.tavewebsite.domain.member.exception.NotMatchedPassword;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.global.mail.dto.MailResponseDto;
import com.tave.tavewebsite.global.mail.service.MailService;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;

    public MailResponseDto saveMember(RegisterManagerRequestDto requestDto) {

        validateNickname(requestDto.nickname());
        validateEmail(requestDto.email());

        Member saveMember = memberRepository.save(Member.toMember(requestDto, passwordEncoder));
        return mailService.sendManagerRegisterMessage(saveMember.getEmail());
    }

    public void saveNormalMember(RegisterMemberRequestDto dto) {
        validateEmail(dto.email());

        memberRepository.save(Member.toNormalMember(dto, passwordEncoder));
    }

    public void deleteMember(long id) {
        memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
        memberRepository.deleteById(id);
    }

    public void sendMessage(ValidateEmailReq req, Boolean reset) {
        if (reset.equals(Boolean.FALSE))
            validateEmail(req.email());
        else
            findIfEmailExists(req.email());

        mailService.sendAuthenticationCode(req.email());
    }

    public void verityNumber(ValidateEmailReq req, Boolean reset) {
        if (reset.equals(Boolean.FALSE))
            validateEmail(req.email());
        else
            findIfEmailExists(req.email());

        String validatedNumber = (String) redisUtil.get(req.email());

        if (!req.number().equals(validatedNumber)) {
            throw new NotMatchedNumberException();
        } else if (redisUtil.checkExpired(req.email()) <= 0 || redisUtil.checkExpired(req.email()) == null) {
            throw new ExpiredNumberException();
        }

        redisUtil.delete(req.email());
    }

    public void resetPassword(ResetPasswordReq req) {
        Member member = memberRepository.findByNickname(req.nickname()).orElseThrow(NotFoundMemberException::new);

        if (!req.password().equals(req.validatedPassword())) {
            throw new NotMatchedPassword();
        }

        member.update(req.validatedPassword(), passwordEncoder);
        memberRepository.save(member);
    }

    public void validateNickname(String nickname) {
        memberRepository.findByNickname(nickname).ifPresent(
                member -> {
                    throw new DuplicateNicknameException();
                }
        );
    }

    private Member findIfEmailExists(String email) {
        return memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);
    }

    private void validateEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(
                member -> {
                    throw new DuplicateEmailException();
                }
        );
    }

    public void verifyNormalMemberForPasswordReset(ResetPasswordVerifyRequestDto req) {
        Member member = memberRepository.findByEmail(req.email())
                .orElseThrow(NotFoundMemberException::new);

        if (!member.getUsername().equals(req.name()) || !member.getBirthday().toString().equals(req.birth())) {
            throw new NotFoundMemberException();
        }

        mailService.sendAuthenticationCode(req.email());
    }

    public void verifyAuthCodeForPasswordReset(String email, String code) {
        String validatedNumber = (String) redisUtil.get(email);

        if (validatedNumber == null || redisUtil.checkExpired(email) <= 0) {
            throw new ExpiredNumberException();
        }

        if (!validatedNumber.equals(code)) {
            throw new NotMatchedNumberException();
        }

        redisUtil.delete(email);
    }

    public void resetNormalMemberPassword(ResetNormalPasswordRequestDto req) {
        Member member = memberRepository.findByEmail(req.email())
                .orElseThrow(NotFoundMemberException::new);

        if (!req.password().equals(req.validatedPassword())) {
            throw new NotMatchedPassword();
        }

        member.update(req.validatedPassword(), passwordEncoder);
    }

}
