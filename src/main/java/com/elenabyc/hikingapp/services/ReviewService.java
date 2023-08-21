package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.ReviewDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    @Transactional
    List<String> addReview(ReviewDto reviewDto, Long userId, Long trailId);

    @Transactional
    List<String> deleteReviewById(Long reviewId);

    List<String> updateReview(ReviewDto reviewDto);

    List<ReviewDto> getAllReviewsByUserId(Long userId);

    List<ReviewDto> getAllReviewsByTrailId(Long trailId);

    Optional<ReviewDto> getReviewById(Long reviewId);
}
