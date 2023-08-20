package com.elenabyc.hikingapp.entities;

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
    private String alias;

    @Column(name = "image_url")
    private String image;

    @OneToMany(mappedBy = "trail", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonManagedReference
    private Set<Review> reviews = new HashSet<>();
}
