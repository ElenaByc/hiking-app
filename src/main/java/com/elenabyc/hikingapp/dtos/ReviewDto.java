package com.elenabyc.hikingapp.dtos;

import com.elenabyc.hikingapp.entities.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto implements Serializable {
    private Long id;
    private String body;
    private int rating;
    private String date;
    private UserDto userDto;
    private TrailDto trailDto;
    private String source;
    private String url;

    public ReviewDto(Review review) {
        if (review.getId() != null) {
            this.id = review.getId();
        }
        if (review.getBody() != null) {
            this.body = review.getBody();
        }
        if (review.getDate() != null) {
            this.date = review.getDate();
        }
        if (review.getRating() != 0) {
            this.rating = review.getRating();
        }
        if (review.getTrail() != null) {
            this.setTrailDto(new TrailDto(review.getTrail()));
        }
        if (review.getUser() != null) {
            this.setUserDto(new UserDto(review.getUser()));
        }
    }
}
