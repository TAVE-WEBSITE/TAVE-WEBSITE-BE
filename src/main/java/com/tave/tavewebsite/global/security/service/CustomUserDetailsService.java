package com.tave.tavewebsite.global.security.service;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.global.security.entity.CustomUserDetails;
import com.tave.tavewebsite.global.security.exception.LoginFailException.EmailNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Member byEmail = memberRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);

        return new CustomUserDetails(byEmail);
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
//    private UserDetails createUserDetails(Member member) {
//        return User.builder()
//                .username(member.getEmail())
//                .password(passwordEncoder.encode(member.getPassword()))
//                .roles("ROLE_" + member.getRole().name())
//                .build();
//    }

}
