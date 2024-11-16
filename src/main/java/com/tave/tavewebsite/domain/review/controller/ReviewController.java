package com.tave.tavewebsite.domain.review.controller;


import com.tave.tavewebsite.domain.review.dto.request.ReviewRequestDto;
import com.tave.tavewebsite.domain.review.dto.response.ReviewResponseDto;
import com.tave.tavewebsite.domain.review.service.ReviewService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tave.tavewebsite.domain.review.controller.SuccessMessage.*;

@Slf4j
@RequestMapping("/api/v1/review")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private static final boolean PUBLIC = true; // true로 값만 쓰임 지양 -> 상수화
    private static final boolean PRIVATE = false; // true로 값만 쓰임 지양 -> 상수화


    @PostMapping
    public SuccessResponse<ReviewResponseDto> registerReview(@RequestBody @Valid ReviewRequestDto requestDto) {
        ReviewResponseDto response = reviewService.saveReview(requestDto);
        return new SuccessResponse<>(
                response,
                REVIEW_CREATE.getMessage(response.generation())
        );
    }

    @GetMapping("/{generation}")
    public SuccessResponse<List<ReviewResponseDto>> getPublicReviews(@PathVariable String generation) {
        List<ReviewResponseDto> response = reviewService.findReviewsByGeneration(generation, PUBLIC);

        return new SuccessResponse<>(
                response,
                REVIEW_GET_PUBLIC.getMessage(generation)
        );
    }

    @GetMapping("/private/{generation}")
    public SuccessResponse<List<ReviewResponseDto>> getPrivateReviews(@PathVariable String generation) {
        List<ReviewResponseDto> response = reviewService.findReviewsByGeneration(generation, PRIVATE);
        return new SuccessResponse<>(
                response,
                REVIEW_GET_PRIVATE.getMessage(generation)
        );    }

    @PatchMapping("/{reviewId}")
    public SuccessResponse updateReview(@PathVariable Long reviewId,
                                                           @RequestBody ReviewRequestDto requestDto) {
        reviewService.updateReview(reviewId, requestDto);
        return SuccessResponse.ok(REVIEW_UPDATE.getMessage());
    }

    @DeleteMapping("/{reviewId}")
    public SuccessResponse deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReivew(reviewId);
        return SuccessResponse.ok(REVIEW_DELETE.getMessage());
    }
}
