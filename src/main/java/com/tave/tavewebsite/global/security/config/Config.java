package com.tave.tavewebsite.global.security.config;

import com.tave.tavewebsite.domain.member.entity.RoleType;
import com.tave.tavewebsite.global.security.filter.CsrfTokenResponseHeaderBindingFilter;
import com.tave.tavewebsite.global.security.filter.JwtAuthenticationFilter;
import com.tave.tavewebsite.global.security.utils.JwtTokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class Config {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "X-XSRF-TOKEN"));
        corsConfiguration.setExposedHeaders(List.of("X-XSRF-TOKEN"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                corsConfigurationSource()));

        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.csrfTokenRepository(
                        CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(), CsrfFilter.class)
        );

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                        // 비회원 전용 api
                        .requestMatchers("/api/v1/manager/signIn").permitAll()
                        // 일반 회원 전용 api
                        .requestMatchers("/member")
                        .hasAnyRole(RoleType.MEMBER.name(), RoleType.UNAUTHORIZED_MANAGER.name(),
                                RoleType.MANAGER.name(), RoleType.ADMIN.name())
                        // 미허가 운영진 전용 api
                        .requestMatchers("/un/manager")
                        .hasAnyRole(RoleType.UNAUTHORIZED_MANAGER.name(), RoleType.MANAGER.name(),
                                RoleType.ADMIN.name())
                        // 운영진 전용 api
                        .requestMatchers("/manager").hasAnyRole(RoleType.MANAGER.name(), RoleType.ADMIN.name())
                        // 회장 전용 api
                        .requestMatchers("/admin").hasRole(RoleType.ADMIN.name())
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        http.formLogin().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
