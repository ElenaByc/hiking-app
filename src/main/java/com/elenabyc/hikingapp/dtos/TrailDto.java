package com.elenabyc.hikingapp.dtos;

import com.elenabyc.hikingapp.entities.Trail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrailDto implements Serializable {
    private Long id;
    private String name;
    private String yelpAlias;
    private String googlePlaceId;
    private String image;
    private Coordinates coordinates;
    private Coordinates googleCoordinates;
    private String address;
    private double yelpRating;
    private double googleRating;
    private int yelpReviewCount;
    private int googleReviewCount;
    private boolean saved;

    private Set<ReviewDto> reviewsDto = new HashSet<>();

    public TrailDto(Trail trail) {
        if (trail.getId() != null) {
            this.id = trail.getId();
        }
        if (trail.getName() != null) {
            this.name = trail.getName();
        }
        if (trail.getYelpAlias() != null) {
            this.yelpAlias = trail.getYelpAlias();
        }
        if (trail.getGooglePlaceId() != null) {
            this.googlePlaceId = trail.getGooglePlaceId();
        }
        if (trail.getImage() != null) {
            this.image = trail.getImage();
        }
        if (trail.getAddress() != null) {
            this.address = trail.getAddress();
        }
    }
}
