package com.tave.tavewebsite.domain.review.mapper;

import com.tave.tavewebsite.domain.review.dto.request.ReviewRequestDto;
import com.tave.tavewebsite.domain.review.dto.response.ReviewManagerResponseDto;
import com.tave.tavewebsite.domain.review.dto.response.ReviewResponseDto;
import com.tave.tavewebsite.domain.review.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toReview(ReviewRequestDto reviewRequestDto) {
        return Review.builder()
                .nickname(reviewRequestDto.nickname())
                .generation(reviewRequestDto.generation())
                .companyName(reviewRequestDto.companyName())
                .field(reviewRequestDto.field())
                .content(reviewRequestDto.content())
                .isPublic(reviewRequestDto.isPublic())
                .build();
    }

    public ReviewResponseDto toReviewResponseDto(Review review) {
        return ReviewResponseDto.builder()
                .nickname(review.getNickname())
                .generation(review.getGeneration())
                .companyName(review.getCompanyName())
                .field(review.getField())
                .content(review.getContent())
                .isPublic(review.isPublic())
                .build();
    }

    public ReviewManagerResponseDto toReviewManagerResponseDto(Review review) {
        return ReviewManagerResponseDto.builder()
                .id(review.getId())
                .nickname(review.getNickname())
                .generation(review.getGeneration())
                .companyName(review.getCompanyName())
                .field(review.getField())
                .content(review.getContent())
                .isPublic(review.isPublic())
                .build();
    }



}
