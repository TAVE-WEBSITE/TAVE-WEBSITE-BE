package com.tave.tavewebsite.domain.programinglaunguage.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgramingLanguage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotNull
    @Column(nullable = false)
    @Size(min = 1, max = 20)
    private String language;

    @NotNull
    @Column(nullable = false)
    @Size(min = 1, max = 8)
    private String field;

    @Builder
    ProgramingLanguage(String language, String field) {
        this.language = language;
        this.field = field;
    }

}
