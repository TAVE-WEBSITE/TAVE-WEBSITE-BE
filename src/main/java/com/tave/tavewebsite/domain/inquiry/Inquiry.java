package com.tave.tavewebsite.domain.inquiry;


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
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(length = 30, nullable = false)
    private String at_id;

    @NotNull
    @Size(max = 30)
    @Column(length = 30, nullable = false)
    private String title;

    @NotNull
    @Column(length = 2083, nullable = false) // DDL varchar(2083)
    private String url;

    @NotNull
    @Column(nullable = false)
    private boolean isPublic;

    @Builder
    public Inquiry(String at_id, String title, String url, boolean isPublic) {
        this.at_id = at_id;
        this.title = title;
        this.url = url;
        this.isPublic = isPublic;
    }
}
