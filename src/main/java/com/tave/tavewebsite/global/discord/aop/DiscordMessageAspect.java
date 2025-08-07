package com.tave.tavewebsite.global.discord.aop;

import com.tave.tavewebsite.domain.member.dto.response.log.MemberLogDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.global.discord.message.DiscordMessage;
import com.tave.tavewebsite.global.discord.service.DiscordService;
import com.tave.tavewebsite.global.security.exception.AuthorizedException;
import com.tave.tavewebsite.global.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DiscordMessageAspect {

    private final DiscordService discordService;

    @Pointcut("@annotation(com.tave.tavewebsite.global.discord.aop.DiscordNotify)")
    public void discordMessagePointcut() {
    }

}
