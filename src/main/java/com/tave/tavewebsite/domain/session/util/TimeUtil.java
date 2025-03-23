package com.tave.tavewebsite.domain.session.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtil {

    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd");
    }
}
