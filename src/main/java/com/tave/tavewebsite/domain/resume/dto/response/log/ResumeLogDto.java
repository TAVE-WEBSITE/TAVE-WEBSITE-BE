package com.tave.tavewebsite.domain.resume.dto.response.log;

import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.global.discord.domain.DiscordMessageType;
import com.tave.tavewebsite.global.discord.domain.Discordable;
import lombok.Builder;
import lombok.Getter;

import static com.tave.tavewebsite.global.discord.domain.DiscordMessageType.RESUME_SUBMIT;

@Getter
@Builder
public class ResumeLogDto implements Discordable {

    private Long resumeId;

    @Override
    public DiscordMessageType getDiscordMessageType() {
        return RESUME_SUBMIT;
    }

    @Override
    public String getMessageIdentifierKey() {
        return "ResumeId" ;
    }

    @Override
    public String getMessageIdentifierValue() {
        return String.valueOf(this.resumeId);
    }

    @Override
    public String getDiscordMessage() {
        return "지원서 최종 제출 완료했습니다.";
    }

    public static ResumeLogDto from(Resume resume) {
        return ResumeLogDto.builder()
                .resumeId(resume.getId())
                .build();
    }
}
