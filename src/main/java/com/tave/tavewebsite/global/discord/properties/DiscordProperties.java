package com.tave.tavewebsite.global.discord.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "discord")
public class DiscordProperties {

    private String webhookUrl;

}
