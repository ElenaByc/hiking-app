package com.elenabyc.hikingapp.entities;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trails")
//@Data
@Setter
@Getter
//@ToString
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

    @OneToMany(mappedBy = "trail", fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonManagedReference
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany(mappedBy = "savedTrails", fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @Override
    public String toString() {
        return "Trail:" + + this.id + " " + this.name;
    }
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
