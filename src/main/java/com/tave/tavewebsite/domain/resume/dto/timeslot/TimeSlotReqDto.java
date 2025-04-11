package com.tave.tavewebsite.domain.resume.dto.timeslot;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TimeSlotReqDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime time
) {
}
