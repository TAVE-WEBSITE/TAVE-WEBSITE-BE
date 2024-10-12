package com.tave.tavewebsite.domain.session.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(length = 30, nullable = false)
    private String title;

    @Column(nullable = false)
    @NotNull
    private String description;

    @NotNull
    @Column(nullable = false)
    private boolean isPublic;

    @NotNull
    @Column(length = 2083, nullable = false) // DDL varchar(2083)
    private String imgUrl;

    @Builder
    public Session(String title, String description, boolean isPublic, String imgUrl) {
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
        this.imgUrl = imgUrl;
    }
}
