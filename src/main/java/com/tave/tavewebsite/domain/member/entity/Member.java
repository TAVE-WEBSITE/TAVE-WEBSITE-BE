package com.tave.tavewebsite.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    @Size(min = 5, max = 255)
    @Column(nullable = false)
    private String email;

    @Size(min = 8, max = 100)
    @Column(nullable = false)
    private String password;

    @Size(min = 4, max = 10)
    @Column(nullable = false)
    private String role;

    @Size(min = 2, max = 20)
    @Column(nullable = false)
    private String nickname;

    @Size(min = 1, max = 30)
    @Column(nullable = false)
    private String agitId;

    @Size(min = 1, max = 5)
    @Column(nullable = false)
    private String generation;

    @Size(min = 1, max = 50)
    @Column(nullable = false)
    private String department;

    @Size(min = 1, max = 50)
    @Column(nullable = false)
    private String job;
}
