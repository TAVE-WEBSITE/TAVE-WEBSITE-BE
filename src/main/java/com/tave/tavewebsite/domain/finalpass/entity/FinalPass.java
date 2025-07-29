package com.tave.tavewebsite.domain.finalpass.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinalPass extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회비
    @NotNull
    @Column(nullable = false)
    private Integer totalFee;

    private Integer clubFee;

    private Integer mtFee;

    @NotNull
    @Column(nullable = false, length = 30)
    private String bankName;

    @NotNull
    @Column(nullable = false, length = 50)
    private String accountNumber;

    @NotNull
    @Column(nullable = false, length = 30)
    private String accountHolder;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime feeDeadline;

    // 아지트 초대 설문 조사
    @NotNull
    @Column(nullable = false)
    private String surveyLink;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime surveyDeadline;

    // OT 공지방
    @NotNull
    @Column(nullable = false)
    private String otLink;

    @NotNull
    @Column(nullable = false)
    private String otPassword;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime otDeadline;
}
