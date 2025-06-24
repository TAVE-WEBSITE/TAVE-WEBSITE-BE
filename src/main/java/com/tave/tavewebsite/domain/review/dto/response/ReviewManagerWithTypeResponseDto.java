package com.tave.tavewebsite.domain.review.dto.response;

import java.util.ArrayList;
import java.util.List;

public record ReviewManagerWithTypeResponseDto(
        List<ReviewManagerResponseDto> reviews,
        List<String> allType
) {
    public static ReviewManagerWithTypeResponseDto of(List<ReviewManagerResponseDto> result, int max) {
        List<String> all = new ArrayList<>();
        System.out.println("max = " + max);
        for (int i = 1; i <= max; i++) {
            all.add(String.valueOf(i));
        }
        return new ReviewManagerWithTypeResponseDto(result, all);
    }
}
