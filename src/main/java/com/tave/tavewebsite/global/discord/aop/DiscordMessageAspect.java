package com.tave.tavewebsite.global.discord.aop;

import com.tave.tavewebsite.domain.member.dto.response.log.MemberLogDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.global.discord.domain.Discordable;
import com.tave.tavewebsite.global.discord.message.DiscordMessage;
import com.tave.tavewebsite.global.discord.service.DiscordService;
import com.tave.tavewebsite.global.security.exception.AuthorizedException;
import com.tave.tavewebsite.global.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DiscordMessageAspect {

    private final DiscordService discordService;

    @Pointcut("@annotation(com.tave.tavewebsite.global.discord.aop.DiscordNotify)")
    public void discordMessagePointcut() {
    }

    @AfterReturning(pointcut = "discordMessagePointcut()", returning = "result")
    public void discordMessageAfterReturning(Object result) {
        try {
            if (result instanceof Discordable returnData) {
                MemberLogDto currentMember = getCurrentMember();
                discordService.sendDiscordMessage(DiscordMessage.from(currentMember, returnData));
            }
        } catch (Exception e) {
            log.error("Discord AOP Error", e);
        }
    }

    public MemberLogDto getCurrentMember() {
        Member currentMember;
        try {
            currentMember = SecurityUtils.getCurrentMember();
        } catch (AuthorizedException e) {
            currentMember = null;
        }
        return MemberLogDto.of(currentMember);
    }
}
