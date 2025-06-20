package com.tave.tavewebsite.global.mail.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class SESMailFormatUtil {

    public String formatRecruitPeriod(LocalDateTime start, LocalDateTime end) {
        return formatKoreanDateTime(start) + " ~ " + formatKoreanDateTime(end);
    }

    public String formatKoreanDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일 (E) HH:mm")
                .withLocale(Locale.KOREAN);
        return dateTime.format(formatter);
    }
}
