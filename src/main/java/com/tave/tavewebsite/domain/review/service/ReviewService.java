package com.tave.tavewebsite.domain.review.service;


import com.tave.tavewebsite.domain.review.dto.request.ReviewRequestDto;
import com.tave.tavewebsite.domain.review.dto.response.ReviewManagerResponseDto;
import com.tave.tavewebsite.domain.review.dto.response.ReviewManagerWithTypeResponseDto;
import com.tave.tavewebsite.domain.review.dto.response.ReviewResponseDto;
import com.tave.tavewebsite.domain.review.entity.Review;
import com.tave.tavewebsite.domain.review.exception.ReviewNotFoundException;
import com.tave.tavewebsite.domain.review.mapper.ReviewMapper;
import com.tave.tavewebsite.domain.review.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    private static final boolean PUBLIC = true; // true로 값만 쓰임 지양 -> 상수화

    public ReviewResponseDto saveReview(ReviewRequestDto requestDto) {
        // 딱히 중복 검사를 할 필드가 없다 -> (동명이인 가능성 有)
        Review saveReview = reviewRepository.save(reviewMapper.toReview(requestDto));

        return reviewMapper.toReviewResponseDto(saveReview);
    }

    public List<ReviewResponseDto> findPublicReviews() {
        List<Review> reviews = reviewRepository.findRandomPublicReviews();

        return reviews.stream()
                .map(reviewMapper::toReviewResponseDto)
                .toList();
    }

    public ReviewManagerWithTypeResponseDto findAllReviewsByGeneration(String generation) {
        List<Review> reviews = reviewRepository.findAll();
        List<Review> re = new ArrayList<>();
        int max = 0;
        for (Review review : reviews) {
            if (review.getGeneration().equals(generation) || generation.equals("ALL")) {
                re.add(review);
            }
            max = Math.max(max, Integer.parseInt(review.getGeneration()));
        }

        List<ReviewManagerResponseDto> list = re.stream()
                .map(reviewMapper::toReviewManagerResponseDto)
                .toList();

        return ReviewManagerWithTypeResponseDto.of(list, max);
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewRequestDto requestDto) {
        Review findReview = findReview(reviewId);
        findReview.update(requestDto);
    }

    public void deleteReivew(Long reviewId) {
        Review findReview = findReview(reviewId);
        reviewRepository.delete(findReview);
    }

    /*
     * 리팩토링
     * */

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
    }
}
