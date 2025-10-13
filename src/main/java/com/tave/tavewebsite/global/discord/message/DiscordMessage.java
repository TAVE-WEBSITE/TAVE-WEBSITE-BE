package com.tave.tavewebsite.global.discord.message;

import com.tave.tavewebsite.domain.member.dto.response.log.MemberLogDto;
import com.tave.tavewebsite.global.discord.domain.Discordable;
import lombok.Builder;

import java.util.List;

@Builder
public record DiscordMessage(
        String content,
        boolean tts,
        List<DiscordEmbed> embeds
) {
    public static DiscordMessage from(MemberLogDto currentMember, Discordable logData) {
        StringBuilder descriptionBuilder = new StringBuilder();

        if (currentMember != null) {
            descriptionBuilder.append("🧑 지원자 이름: ").append(currentMember.getUserName()).append("\n");
            descriptionBuilder.append("📧 지원자 이메일: ").append(currentMember.getEmail()).append("\n");
        }

        descriptionBuilder.append("🔑 ")
                .append(logData.getMessageIdentifierKey())
                .append(": ")
                .append(logData.getMessageIdentifierValue())
                .append("\n");

        descriptionBuilder.append("📝 ")
                .append(logData.getDiscordMessage())
                .append("\n");

        DiscordEmbed embed = DiscordEmbed.builder()
                .title("✅ " + logData.getDiscordMessageType().display)
                .description(descriptionBuilder.toString())
                .color(5763719)
                .build();

        return DiscordMessage.builder()
                .content("📣 새 알림이 도착했습니다!")
                .tts(false)
                .embeds(List.of(embed))
                .build();
    }
}
