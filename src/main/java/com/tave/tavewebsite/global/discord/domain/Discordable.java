package com.tave.tavewebsite.global.discord.domain;

/*
* 전달하고자 하는 Identifier의 Key:Value 전달
* getDiscordMessage() - Discord에 전달하고자 하는 내용 작성
* */

public interface Discordable {

    DiscordMessageType getDiscordMessageType();
    String getMessageIdentifierKey();
    String getMessageIdentifierValue();
    String getDiscordMessage();

}
