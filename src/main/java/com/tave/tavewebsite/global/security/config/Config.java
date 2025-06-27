package com.tave.tavewebsite.global.security.config;

import com.tave.tavewebsite.domain.member.entity.RoleType;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import com.tave.tavewebsite.global.security.exception.CustomAuthenticationEntryPoint;
import com.tave.tavewebsite.global.security.filter.JwtAuthenticationFilter;
import com.tave.tavewebsite.global.security.utils.JwtTokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class Config {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(
                List.of("http://localhost:3000", "https://localhost:3000", "http://localhost:8080",
                        "http://localhost:5173", "https://localhost:5173",
                        "https://www.tave-wave.com", "https://tave-admin.info", "https://www.tave-admin.info",
                        "https://recruit.tave-wave.com",
                        "https://tave-apply-rjjft5des-taves-projects-79609e27.vercel.app",
                        "https://test.tave-wave.com/"
                ));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setExposedHeaders(List.of("Set-Cookie"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                corsConfigurationSource()));

        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                        // 비회원 전용 api
                        .requestMatchers("/v1/normal/**", "/v1/auth/signup", "/v1/auth/signin", "/v1/auth/refresh",
                                "/v1/auth/normal/signup")
                        .permitAll()

                        // 일반 회원 전용 api
                        .requestMatchers("/v1/member/**", "/v1/auth/signout", "/v1/auth/delete/**")
                        .hasAnyRole(RoleType.MEMBER.name(), RoleType.UNAUTHORIZED_MANAGER.name(),
                                RoleType.MANAGER.name(), RoleType.VICE_PRESIDENT.name(), RoleType.ADMIN.name())

                        // 미허가 운영진 전용 api
                        .requestMatchers("/v1/xxxxxxxxx")
                        .hasAnyRole(RoleType.UNAUTHORIZED_MANAGER.name(), RoleType.MANAGER.name(),
                                RoleType.VICE_PRESIDENT.name(), RoleType.ADMIN.name())

                        // 운영진 전용 api
                        .requestMatchers("/v1/manager/**")
                        .hasAnyRole(RoleType.MANAGER.name(), RoleType.VICE_PRESIDENT.name(), RoleType.ADMIN.name())

                        // 처장+회장 전용 api
                        .requestMatchers("/v1/admin/config/**")
                        .hasAnyRole(RoleType.VICE_PRESIDENT.name(), RoleType.ADMIN.name())

                        // 회장 전용 api
                        .requestMatchers("/v1/admin/**").hasRole(RoleType.ADMIN.name())
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisUtil),
                UsernamePasswordAuthenticationFilter.class);

        http.formLogin().disable();

        http.exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
