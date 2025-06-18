package com.tave.tavewebsite.domain.session.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tave.tavewebsite.domain.session.dto.request.SessionRequestDto;
import com.tave.tavewebsite.domain.session.util.TimeUtil;
import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;


@Entity
@Getter
@SuperBuilder
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

    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate eventDay;

    @Enumerated(EnumType.STRING)
    private Period period;

    @NotNull
    @URL
    @Column(length = 2083, nullable = false) // DDL varchar(2083)
    private String imgUrl;

    public static Session of(SessionRequestDto sessionRequestDto, java.net.URL imgUrl, TimeUtil timeUtil) {
        return Session.builder()
                .title(sessionRequestDto.title())
                .description(sessionRequestDto.description())
                .eventDay(LocalDate.parse(sessionRequestDto.eventDay(), timeUtil.getFormatter()))
                .imgUrl(imgUrl.toString())
                .period(sessionRequestDto.period())
                .build();
    }

    public void updateField(SessionRequestDto sessionRequestDto, TimeUtil timeUtil) {
        this.title = sessionRequestDto.title();
        this.description = sessionRequestDto.description();
        this.eventDay = LocalDate.parse(sessionRequestDto.eventDay(), timeUtil.getFormatter());
        this.period = sessionRequestDto.period();
    }

    public void updateImgUrl( java.net.URL imgUrl) {
        this.imgUrl = imgUrl.toString();
    }
}
