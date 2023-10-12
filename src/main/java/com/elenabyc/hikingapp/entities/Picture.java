package com.elenabyc.hikingapp.entities;

import com.elenabyc.hikingapp.dtos.PictureDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pictures")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_id")
    private Long id;

    @Column(name = "picture_url")
    private String url;

    @ManyToOne
    @JsonBackReference
    private User user;

    @ManyToOne
    @JsonBackReference
    private Trail trail;

    public Picture(PictureDto pictureDto) {
        if (pictureDto.getUrl() != null) {
            this.url = pictureDto.getUrl();
        }
    }
}
