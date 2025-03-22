package com.tave.tavewebsite.domain.review.repository;

import com.tave.tavewebsite.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT * FROM review WHERE is_public = true ORDER BY RANDOM() LIMIT 8", nativeQuery = true)
    List<Review> findRandomPublicReviews();

    List<Review> findByGeneration(String generation);

}
