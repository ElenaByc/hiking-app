package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.ReviewDto;
import com.elenabyc.hikingapp.entities.Review;
import com.elenabyc.hikingapp.entities.Trail;
import com.elenabyc.hikingapp.entities.User;
import com.elenabyc.hikingapp.repositories.ReviewRepository;
import com.elenabyc.hikingapp.repositories.TrailRepository;
import com.elenabyc.hikingapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TrailRepository trailRepository;
    @Autowired
    private TrailService trailService;

    @Override
    @Transactional
    public List<String> addReview(ReviewDto reviewDto, long userId) {
        List<String> response = new ArrayList<>();
        Optional<User> userOptional = userRepository.findById(userId);
        Trail trail = trailService.addTrail(reviewDto.getTrailDto());
        Review review = new Review(reviewDto);
        if (userOptional.isEmpty()) {
            response.add("Error: there is no user with id = " + userId);
            return response;
        }
        userOptional.ifPresent(review::setUser);
        review.setTrail(trail);
        reviewRepository.saveAndFlush(review);
        response.add("Review added successfully");
        return response;
    }

    @Override
    @Transactional
    public List<String> deleteReviewById(Long reviewId) {
        List<String> response = new ArrayList<>();
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isEmpty()) {
            response.add("Error: there is no review with id = " + reviewId);
        }
        reviewOptional.ifPresent(review -> {
            reviewRepository.delete(review);
            response.add("Review with id = " + reviewId + " was deleted");
        });
        return response;
    }

    @Override
    public List<String> updateReview(ReviewDto reviewDto) {
        List<String> response = new ArrayList<>();
        Optional<Review> reviewOptional = reviewRepository.findById(reviewDto.getId());
        if (reviewOptional.isEmpty()) {
            response.add("Error: there is no review with id = " + reviewDto.getId());
        }
        reviewOptional.ifPresent(review -> {
            review.setBody(reviewDto.getBody());
            review.setDate(reviewDto.getDate());
            reviewRepository.saveAndFlush(review);
            response.add("Review with id = " + review.getId() + " was updated");
        });
        return response;
    }

    @Override
    public List<ReviewDto> getAllReviewsByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            List<Review> reviewList = reviewRepository.findAllByUserEquals(userOptional.get());
            return reviewList.stream().map(ReviewDto::new).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<ReviewDto> getAllReviewsByTrailId(Long trailId) {
        Optional<Trail> trailOptional = trailRepository.findById(trailId);
        if (trailOptional.isPresent()) {
            List<Review> reviewList = reviewRepository.findAllByTrailEquals(trailOptional.get());
            return reviewList.stream().map(ReviewDto::new).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<ReviewDto> getReviewById(Long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        return reviewOptional.map(ReviewDto::new);
    }
}
