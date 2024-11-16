package com.tave.tavewebsite.domain.review.service;


import com.tave.tavewebsite.domain.review.exception.ReviewNotFoundException;
import com.tave.tavewebsite.domain.review.mapper.ReviewMapper;
import com.tave.tavewebsite.domain.review.dto.request.ReviewRequestDto;
import com.tave.tavewebsite.domain.review.dto.response.ReviewResponseDto;
import com.tave.tavewebsite.domain.review.entity.Review;
import com.tave.tavewebsite.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewResponseDto saveReview(ReviewRequestDto requestDto) {
        // 딱히 중복 검사를 할 필드가 없다 -> (동명이인 가능성 有)
        Review saveReview = reviewRepository.save(reviewMapper.toReview(requestDto));

        return reviewMapper.toReviewResponseDto(saveReview);
    }

    public List<ReviewResponseDto> findReviewsByGeneration(String generation, boolean isPublic) {
        List<Review> reviews = reviewRepository.findByGenerationAndIsPublic(generation, isPublic);

        return reviews.stream()
                .map(reviewMapper::toReviewResponseDto)
                .toList();
    }

    @Transactional
    public void updateReview(Long reviewId,ReviewRequestDto requestDto) {
        Review findReview = findReview(reviewId);
        findReview.update(requestDto);
    }

    /*
    * 리팩토링
    * */

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
    }
}
