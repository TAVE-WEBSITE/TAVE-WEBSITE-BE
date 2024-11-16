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

    @PostMapping
    public SuccessResponse<ReviewResponseDto> registerReview(@RequestBody @Valid ReviewRequestDto requestDto) {
        ReviewResponseDto response = reviewService.saveReview(requestDto);
        return new SuccessResponse<>(
                response,
                REVIEW_CREATE.getMessage(response.generation())
        );
    }

    @GetMapping("/{generation}")
    public SuccessResponse<List<ReviewResponseDto>> getReviews(@PathVariable String generation) {
        List<ReviewResponseDto> response = reviewService.findReviewsByGeneration(generation);

        return new SuccessResponse<>(
                response,
                REVIEW_GET.getMessage(generation)
        );
    }

    @PatchMapping("/{reviewId}")
    public SuccessResponse updateReview(@PathVariable Long reviewId,
                                                           @RequestBody ReviewRequestDto requestDto) {
        reviewService.updateReview(reviewId, requestDto);
        return SuccessResponse.ok(REVIEW_UPDATE.getMessage());
    }
}
