package com.tave.tavewebsite.domain.session.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Size(max = 30)
    private String title;

    private String description;

    private boolean is_public;

    @Column(length = 2083) // DDL varchar(2083)
    private String img_url;

    @Builder
    public Session(String title, String description, boolean is_public, String img_url) {
        this.title = title;
        this.description = description;
        this.is_public = is_public;
        this.img_url = img_url;
    }
}
