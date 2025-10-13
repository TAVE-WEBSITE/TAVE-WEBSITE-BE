package com.tave.tavewebsite.global.discord.message;

import lombok.Builder;

@Builder
public record DiscordEmbed(
        String title,
        String description,
        Integer color
) { }