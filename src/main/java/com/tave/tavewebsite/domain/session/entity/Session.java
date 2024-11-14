package com.tave.tavewebsite.domain.session.entity;

import com.tave.tavewebsite.domain.session.dto.request.SessionRequestDto;
import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sessionId")
    private Long id;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(length = 30, nullable = false)
    private String title;

    @NotNull
    @Size(min = 1, max = 500)
    @Column(nullable = false)
    private String description;

    /*
    * 동아리 지원 관리자 페이지에서 사용 시 "세션 진행 날짜" 필드를 추가하는 게 좋다고 생각.
    * */

    @Size(min = 1, max = 5)
    @Column(length = 5)
    private String generation;

    @NotNull
    @Column(nullable = false)
    private boolean isPublic;

    @NotNull
    @URL
    @Column(length = 2083, nullable = false) // DDL varchar(2083)
    private String imgUrl;

    @Builder
    public Session(String title, String description, String generation,boolean isPublic, String imgUrl) {
        this.title = title;
        this.description = description;
        this.generation = generation;
        this.isPublic = isPublic;
        this.imgUrl = imgUrl;
    }

    public static Session of(SessionRequestDto sessionRequestDto, java.net.URL imgUrl) {
        return Session.builder()
                .title(sessionRequestDto.title())
                .description(sessionRequestDto.description())
                .generation(sessionRequestDto.generation())
                .isPublic(sessionRequestDto.isPublic())
                .imgUrl(imgUrl.toString())
                .build();
    }

    public void updateField(SessionRequestDto sessionRequestDto) {
        this.title = sessionRequestDto.title();
        this.description = sessionRequestDto.description();
        this.generation = sessionRequestDto.generation();
        this.isPublic = sessionRequestDto.isPublic();
    }

    public void updateImgUrl( java.net.URL imgUrl) {
        this.imgUrl = imgUrl.toString();
    }
}
