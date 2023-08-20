package com.elenabyc.hikingapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "yelp_alias", unique = true)
    private String alias;

    @Column(name = "image_url")
    private String image;
}
