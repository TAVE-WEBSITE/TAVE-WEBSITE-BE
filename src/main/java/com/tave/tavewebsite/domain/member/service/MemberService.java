package com.tave.tavewebsite.domain.member.service;

import static com.amazonaws.services.ec2.model.PrincipalType.Role;
import static com.tave.tavewebsite.domain.member.entity.RoleType.MANAGER;
import static com.tave.tavewebsite.domain.member.entity.RoleType.UNAUTHORIZED_MANAGER;

import com.tave.tavewebsite.domain.member.dto.request.*;
import com.tave.tavewebsite.domain.member.dto.response.AuthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.SignInResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.UnauthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.exception.*;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.global.mail.dto.MailResponseDto;
import com.tave.tavewebsite.global.mail.service.MailService;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import com.tave.tavewebsite.global.security.entity.JwtToken;
import com.tave.tavewebsite.global.security.exception.JwtValidException.NotMatchRefreshTokenException;
import com.tave.tavewebsite.global.security.utils.JwtTokenProvider;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
    private final RedisUtil redisUtil;

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

    public SignInResponseDto signIn(SignUpRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                requestDto.email(),
                requestDto.password());
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return SignInResponseDto.from(generateToken(requestDto.email()),
                memberRepository.findByEmail(requestDto.email()).get());

    }

    public void singOut(String accessToken) {
        redisUtil.setBlackList(accessToken, "ban accessToken", 30);
    }

    public JwtToken refreshToken(RefreshTokenRequestDto requestDto) {
        Object refreshToken = redisUtil.get(requestDto.email());
        if (!refreshToken.equals(requestDto.refreshToken())) {
            throw new NotMatchRefreshTokenException();
        }
        return generateToken(requestDto.email());
    }

    public void deleteMember(long id) {
        memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
        memberRepository.deleteById(id);
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

    private JwtToken generateToken(String email) {
        JwtToken jwtToken = jwtTokenProvider.generateToken(
                memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new));
        redisUtil.set(email, jwtToken.getRefreshToken(), 120);

        return jwtToken;
    }

    public void sendMessage(ValidateEmailReq req) {
        memberRepository.findByNickname(req.nickname()).orElseThrow(NotFoundMemberException::new);
        memberRepository.findByEmail(req.email()).orElseThrow(NotFoundMemberException::new);

        mailService.sendAuthenticationCode(req.email());
    }

    public void verityNumber(ValidateEmailReq req){
        memberRepository.findByEmail(req.email()).orElseThrow(NotFoundMemberException::new);
        String validatedNumber = (String) redisUtil.get(req.email());

        if(!req.number().equals(validatedNumber)){
            throw new NotMatchedNumberException();
        }
        else if(redisUtil.checkExpired(req.email()) <= 0 || redisUtil.checkExpired(req.email()) == null){
            throw new ExpiredNumberException();
        }

        redisUtil.delete(req.email());
    }

    public void resetPassword(ResetPasswordReq req){
        Member member = memberRepository.findByNickname(req.nickname()).orElseThrow(NotFoundMemberException::new);

        if(!req.password().equals(req.validatedPassword())){
            throw new NotMatchedPassword();
        }

        member.update(req.validatedPassword(), passwordEncoder);
        memberRepository.save(member);
    }

    public void updateAuthentication(String memberId){
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

}
