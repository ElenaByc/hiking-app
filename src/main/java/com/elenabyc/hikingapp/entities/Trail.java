package com.elenabyc.hikingapp.entities;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trails")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trail_id")
    private Long id;

    @Column(name = "trail_name")
    private String name;

    @Column(name = "yelp_alias", unique = true, nullable = false)
    private String yelpAlias;

    @Column(name = "google_place_id")
    private String googlePlaceId;

    @Column(name = "image_url")
    private String image;

    @OneToMany(mappedBy = "trail", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonManagedReference
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany(mappedBy = "savedTrails")
    private Set<User> users = new HashSet<>();

    public Trail(TrailDto trailDto) {
        if (trailDto.getName() != null) {
            this.name = trailDto.getName();
        }
        if (trailDto.getYelpAlias() != null) {
            this.yelpAlias = trailDto.getYelpAlias();
        }
        if (trailDto.getGooglePlaceId() != null) {
            this.googlePlaceId = trailDto.getGooglePlaceId();
        }
        if (trailDto.getImage() != null) {
            this.image = trailDto.getImage();
        }
    }
}
