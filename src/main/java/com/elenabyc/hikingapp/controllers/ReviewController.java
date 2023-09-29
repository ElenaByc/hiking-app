package com.elenabyc.hikingapp.controllers;

import com.elenabyc.hikingapp.dtos.ReviewDto;
import com.elenabyc.hikingapp.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/save/{userId}")
    public List<String> addReview(@RequestBody ReviewDto reviewDto, @PathVariable long userId) {
        return reviewService.addReview(reviewDto, userId);
    }
}
