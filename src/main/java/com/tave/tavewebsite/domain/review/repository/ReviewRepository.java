package com.tave.tavewebsite.domain.review.repository;

import com.tave.tavewebsite.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByGenerationAndIsPublic(String generation, boolean isPublic);
}
