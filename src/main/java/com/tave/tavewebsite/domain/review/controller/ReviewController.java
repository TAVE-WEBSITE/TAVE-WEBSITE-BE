package com.tave.tavewebsite.domain.review.controller;


import com.tave.tavewebsite.domain.review.dto.request.ReviewRequestDto;
import com.tave.tavewebsite.domain.review.dto.response.ReviewManagerResponseDto;
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
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/manager/review")
    public SuccessResponse<ReviewResponseDto> registerReview(@RequestBody @Valid ReviewRequestDto requestDto) {
        ReviewResponseDto response = reviewService.saveReview(requestDto);
        return new SuccessResponse<>(
                response,
                REVIEW_CREATE.getMessage(response.generation())
        );
    }

    @GetMapping("/manager/review/{generation}")
    public SuccessResponse<List<ReviewManagerResponseDto>> getAllReviews(@PathVariable String generation) {
        List<ReviewManagerResponseDto> response = reviewService.findAllReviewsByGeneration(generation);

        return new SuccessResponse<>(
                response,
                REVIEW_GET_MANAGER.getMessage(generation)
        );
    }

    // 비회원이 후기 조회
    @GetMapping("/normal/review")
    public SuccessResponse<List<ReviewResponseDto>> getPublicReviews() {
        List<ReviewResponseDto> response = reviewService.findPublicReviews();
        return new SuccessResponse<>(
                response,
                REVIEW_GET_NORMAL.getMessage()
        );
    }

    @PatchMapping("/manager/review/{reviewId}")
    public SuccessResponse updateReview(@PathVariable Long reviewId,
                                        @RequestBody ReviewRequestDto requestDto) {
        reviewService.updateReview(reviewId, requestDto);
        return SuccessResponse.ok(REVIEW_UPDATE.getMessage());
    }

    @DeleteMapping("/manager/review/{reviewId}")
    public SuccessResponse deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReivew(reviewId);
        return SuccessResponse.ok(REVIEW_DELETE.getMessage());
    }
}
