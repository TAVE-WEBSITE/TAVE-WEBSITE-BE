package com.tave.tavewebsite.domain.review.mapper;

import com.tave.tavewebsite.domain.review.dto.request.ReviewRequestDto;
import com.tave.tavewebsite.domain.review.dto.response.ReviewResponseDto;
import com.tave.tavewebsite.domain.review.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toReview(ReviewRequestDto reviewRequestDto) {
        return Review.builder()
                .nickname(reviewRequestDto.nickname())
                .generation(reviewRequestDto.generation())
                .field(reviewRequestDto.field())
                .content(reviewRequestDto.content())
                .isPublic(reviewRequestDto.isPublic())
                .build();
    }

    public ReviewResponseDto toReviewResponseDto(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .nickname(review.getNickname())
                .generation(review.getGeneration())
                .field(review.getField())
                .content(review.getContent())
                .isPublic(review.isPublic())
                .build();
    }



}
