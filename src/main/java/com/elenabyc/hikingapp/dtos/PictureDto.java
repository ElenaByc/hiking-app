package com.elenabyc.hikingapp.dtos;

import com.elenabyc.hikingapp.entities.Picture;
import com.elenabyc.hikingapp.entities.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PictureDto implements Serializable {
    private Long id;
    private String url;
    private UserDto userDto;
    private TrailDto trailDto;
    private String source;

    public PictureDto(Picture picture) {
        if (picture.getId() != null) {
            this.id = picture.getId();
        }
        if (picture.getUrl() != null) {
            this.url = picture.getUrl();
        }
        if (picture.getTrail() != null) {
            this.setTrailDto(new TrailDto(picture.getTrail()));
        }
        if (picture.getUser() != null) {
            this.setUserDto(new UserDto(picture.getUser()));
        }
    }
}
