package com.tave.tavewebsite.domain.member.dto.response.log;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.global.discord.aop.DiscordMessageType;
import com.tave.tavewebsite.global.discord.aop.Discordable;
import lombok.Builder;
import lombok.Getter;

import static com.tave.tavewebsite.global.discord.aop.DiscordMessageType.SIGN_UP;

@Getter
@Builder
public class MemberLogDto implements Discordable {

    private Long memberId;
    private String userName;
    private String email;

    @Override
    public DiscordMessageType getDiscordMessageType() {
        return SIGN_UP;
    }

    @Override
    public String getDiscordMessage() {
        return new StringBuilder()
                .append("🧑 지원자 이름: ").append(this.userName).append("\n")
                .append("📧 지원자 이메일: ").append(this.email).append("\n")
                .append("회원가입을 완료했습니다.").toString();
    }

    @Override
    public String getMessageIdentifierKey() {
        return "MemberId";
    }

    @Override
    public String getMessageIdentifierValue() {
        return String.valueOf(this.memberId);
    }

    public static MemberLogDto of(Member member) {

        if (member == null) {
            return null;
        }

        return MemberLogDto.builder()
                .memberId(member.getId())
                .userName(member.getUsername())
                .email(member.getEmail())
                .build();
    }
}
