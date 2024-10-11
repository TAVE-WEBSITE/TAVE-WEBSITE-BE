package com.tave.tavewebsite.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseProgramEntity {

    @Size(max = 30)
    private String title;

    private String description;

    @Size(max=5)
    private String generation;

    @Size(max = 30)
    private String field;

    private LocalDateTime start_date;

    private LocalDateTime end_date;

    @Column(length = 2083) // DDL varchar(2083)
    private String blog_url;

    @Column(length = 2083) // DDL varchar(2083)
    private String img_url;

}
