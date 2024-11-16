package com.tave.tavewebsite.domain.review.repository;

import com.tave.tavewebsite.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
