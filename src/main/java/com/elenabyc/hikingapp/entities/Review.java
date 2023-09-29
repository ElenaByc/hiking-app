package com.elenabyc.hikingapp.entities;

import com.elenabyc.hikingapp.dtos.ReviewDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "review_body", columnDefinition = "text")
    private String body;

    @Column(name = "date_created", nullable = false)
    private String date;

    @Column(name = "rating", nullable = false)
    private int rating;
    

    @ManyToOne
    @JsonBackReference
    private User user;

    @ManyToOne
    @JsonBackReference
    private Trail trail;

    public Review(ReviewDto reviewDto) {
        if (reviewDto.getBody() != null) {
            this.body = reviewDto.getBody();
        }
        if (reviewDto.getRating() != 0) {
            this.rating = reviewDto.getRating();
        }
        if (reviewDto.getDate() != null) {
            this.date = reviewDto.getDate();
        }
    }
}
