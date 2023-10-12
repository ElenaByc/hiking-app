package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.ReviewDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReviewService {
    @Transactional
    List<String> addReview(ReviewDto reviewDto, long userId);

    @Transactional
    List<String> deleteReviewById(long reviewId);

    List<String> updateReview(ReviewDto reviewDto);

    List<ReviewDto> getAllReviewsByUserId(long userId);

    List<ReviewDto> getAllReviewsByTrailId(Long trailId);

}
