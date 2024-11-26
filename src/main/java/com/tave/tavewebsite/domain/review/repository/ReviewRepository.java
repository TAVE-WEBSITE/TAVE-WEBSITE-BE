package com.tave.tavewebsite.domain.review.repository;

import com.tave.tavewebsite.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByGeneration(String generation);

    List<Review> findByIsPublic(boolean isPublic);
}
