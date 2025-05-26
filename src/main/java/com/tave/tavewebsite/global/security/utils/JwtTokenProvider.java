package com.tave.tavewebsite.global.security.utils;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.exception.NotFoundMemberException;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.global.security.entity.CustomUserDetails;
import com.tave.tavewebsite.global.security.entity.JwtToken;
import com.tave.tavewebsite.global.security.exception.JwtValidException.CannotUseRefreshToken;
import com.tave.tavewebsite.global.security.exception.JwtValidException.EmptyClaimsException;
import com.tave.tavewebsite.global.security.exception.JwtValidException.ExpiredTokenException;
import com.tave.tavewebsite.global.security.exception.JwtValidException.InValidTokenException;
import com.tave.tavewebsite.global.security.exception.JwtValidException.UnsupportedTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final MemberRepository memberRepository;

    // application.yml에서 secret 값 가져와서 key에 저장
    public JwtTokenProvider(@Value("${JWT_SECRET}") String secretKey, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public JwtToken generateToken(Member member) {

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 1800000);
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim("auth", "ROLE_" + member.getRole().name())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(HttpServletRequest request, String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            request.setAttribute("cannotUseRefreshToken", 400);
            throw new CannotUseRefreshToken();
        }

        // 클레임에서 권한 정보 가져오기
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority((String) claims.get("auth")));

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        Member member = memberRepository.findById(Long.valueOf(claims.getSubject())).orElseThrow(NotFoundMemberException::new);
        CustomUserDetails userDetails = new CustomUserDetails(member);

        // 3) AuthenticationToken 에 principal 로 CustomUserDetails 넣기
        return new UsernamePasswordAuthenticationToken(
                userDetails,                          // <- CustomUserDetails
                accessToken,                          // credentials 필드에 토큰을 넣어도 되고, null 여도 됩니다
                userDetails.getAuthorities()          // 권한
        );
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(HttpServletRequest request, String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("invalidJwtToken", 401);
            throw new InValidTokenException();
        } catch (ExpiredJwtException e) {
            request.setAttribute("expiredJwtToken", 401);
            throw new ExpiredTokenException();
        } catch (UnsupportedJwtException e) {
            request.setAttribute("unsupportedJwtToken", 401);
            throw new UnsupportedTokenException();
        } catch (IllegalArgumentException e) {
            throw new EmptyClaimsException();
        }
    }


    // accessToken
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}