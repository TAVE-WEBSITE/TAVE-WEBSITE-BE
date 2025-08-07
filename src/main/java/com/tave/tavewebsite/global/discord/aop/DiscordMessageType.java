package com.tave.tavewebsite.global.discord.aop;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DiscordMessageType {

    SIGN_UP("[회원가입]"),
    RESUME_SUBMIT("[지원서 최종 제출]");

    public final String display;

}
