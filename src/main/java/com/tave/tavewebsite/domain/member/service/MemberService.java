package com.tave.tavewebsite.domain.member.service;

import com.tave.tavewebsite.domain.member.dto.request.*;
import com.tave.tavewebsite.domain.member.dto.response.MemberResumeDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.exception.*;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.global.mail.service.MailService;
import com.tave.tavewebsite.global.mail.service.SESMailService;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final SESMailService sesMailService;

    public void saveMember(RegisterManagerRequestDto requestDto) {

        validateNickname(requestDto.nickname());
        validateEmail(requestDto.email());

        Member saveMember = memberRepository.save(Member.toMember(requestDto, passwordEncoder));
        sesMailService.sendAdminApplySuccessNotification(saveMember.getEmail(), saveMember.getUsername());
    }

    public void saveNormalMember(RegisterMemberRequestDto dto) {
        validateEmail(dto.email());

        Member normalMember = Member.toNormalMember(dto, passwordEncoder);

        memberRepository.save(normalMember);
        sesMailService.sendJoinSuccessNotification(normalMember.getEmail(), normalMember.getUsername(), normalMember.getEmail());
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

        String code = generateCode();
        sesMailService.sendEmailAuthenticationCode(req.email(), code);
        redisUtil.set(req.email(), code, 3);
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

    public List<MemberResumeDto> findMemberResumeDto(String generation, List<String> email){
        return memberRepository.findMemberIdAndResumeIdByGenAndEmails(generation, email);
    }

    private String generateCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // 6자리의 랜덤한 코드를 만든다.
    }
}
