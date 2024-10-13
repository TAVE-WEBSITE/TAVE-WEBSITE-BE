package com.tave.tavewebsite.domain.member.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @NotNull
    @Size(min = 4, max = 10)
    @Column(length = 10, nullable = false)
    private String role;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(length = 20, nullable = false)
    private String nickname;

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
    public Member(String email, String password, String role, String nickname, String agitId, String generation, DepartmentType department, JobType job) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.agitId = agitId;
        this.generation = generation;
        this.department = department;
        this.job = job;
    }
}
