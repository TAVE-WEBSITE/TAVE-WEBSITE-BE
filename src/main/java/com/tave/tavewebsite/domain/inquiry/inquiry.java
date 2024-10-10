package com.tave.tavewebsite.domain.inquiry;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @Size(max = 30)
    private String at_id;

    @Size(max = 30)
    private String title;

    @Column(length = 2083) // DDL varchar(2083)
    private String url;

    private boolean is_public;
}
