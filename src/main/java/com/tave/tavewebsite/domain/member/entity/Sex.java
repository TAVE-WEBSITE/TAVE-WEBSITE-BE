package com.tave.tavewebsite.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sex {
    MALE("남자"),
    FEMALE("여자"),
    NULL("분석 실패"),
    NON_SEX("분석 실패");

    private final String displayName;
}
