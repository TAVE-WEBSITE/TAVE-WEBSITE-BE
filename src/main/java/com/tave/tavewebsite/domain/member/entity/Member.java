package com.tave.tavewebsite.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tave.tavewebsite.domain.member.dto.request.RegisterManagerRequestDto;
import com.tave.tavewebsite.domain.member.dto.request.RegisterMemberRequestDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.tave.tavewebsite.domain.member.entity.RoleType.*;

@Entity
@Getter
@SuperBuilder
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

    @Size(min = 1, max = 13)
    @Column(length = 13)
    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Resume> resumes = new ArrayList<>();

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

    public static Member toNormalMember(RegisterMemberRequestDto dto, PasswordEncoder encoder) {
        return Member.builder()
                .email(dto.email())
                .username(dto.username())
                .phoneNumber(dto.phoneNumber())
                .password(encoder.encode(dto.password()))
                .birthday(dto.getBirthdayAsLocalDate())
                .role(MEMBER)
                .build();
    }

    public void update(String validatedPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(validatedPassword);
    }

    public void updateRole() {
        this.role = MANAGER;
    }

    public void addResume(Resume resume) {
        this.resumes.add(resume);
    }
}
