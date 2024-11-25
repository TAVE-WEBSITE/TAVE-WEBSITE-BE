package com.tave.tavewebsite.domain.member.entity;

import static com.tave.tavewebsite.domain.member.entity.RoleType.UNAUTHORIZED_MANAGER;

import com.tave.tavewebsite.domain.member.dto.request.RegisterManagerRequestDto;
import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    @NotNull
    @Size(min = 5, max = 255)
    @Column(nullable = false)
    private String email;

    @NotNull
    @Size(min = 8, max = 100)
    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(length = 20, nullable = false)
    private String nickname;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(length = 20, nullable = false)
    private String username;

    @Size(min = 1, max = 30)
    @Column(length = 30)
    private String agitId;

    @Size(min = 1, max = 5)
    @Column(length = 5)
    private String generation;

    @Enumerated(EnumType.STRING)
    private DepartmentType department;

    @Enumerated(EnumType.STRING)
    private JobType job;


    @Builder
    public Member(String email, String password, RoleType role, String nickname, String username, String agitId,
                  String generation, DepartmentType department, JobType job) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.username = username;
        this.agitId = agitId;
        this.generation = generation;
        this.department = department;
        this.job = job;
    }

    // 패스워드 인코딩 필요
    public static Member toMember(RegisterManagerRequestDto registerManagerRequestDto,
                                  PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(registerManagerRequestDto.email())
                .password(passwordEncoder.encode(registerManagerRequestDto.password()))
                .agitId(registerManagerRequestDto.agitId())
                .nickname(registerManagerRequestDto.nickname())
                .username(registerManagerRequestDto.username())
                .generation(registerManagerRequestDto.generation())
                .role(UNAUTHORIZED_MANAGER)
                .job(registerManagerRequestDto.job())
                .department(registerManagerRequestDto.department())
                .build();
    }
}
