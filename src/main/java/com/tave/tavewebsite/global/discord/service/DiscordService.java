package com.tave.tavewebsite.global.discord.service;

import com.tave.tavewebsite.global.discord.message.DiscordMessage;
import com.tave.tavewebsite.global.discord.properties.DiscordProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordService {

    private final DiscordProperties properties;
    private final WebClient discordWebClient;

    public void sendDiscordMessage(DiscordMessage discordMessage) {
        discordWebClient.post()
                .uri(properties.getWebhookUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(discordMessage)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("✅ Discord 메시지 전송 성공"))
                .doOnError(error -> log.error("❌ Discord 메시지 전송 실패", error))
                .subscribe();
    }

}
