package com.tave.tavewebsite.domain.study.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    @Size(max = 30)
    private String title;

    private String description; // 스터디 주제

    @Size(max=5)
    private String generation;

    @Size(max = 30)
    private String field;

    private LocalDateTime start_date;

    private LocalDateTime end_date;

    @Column(length = 2083) // DDL varchar(2083)
    private URL blog_url;

    @Column(length = 2083) // DDL varchar(2083)
    private URL img_url;
}
