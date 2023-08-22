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
    private String alias;
    private String image;
//    private String phone;
//    private String location;
//    private String[] openHours;

    private Set<ReviewDto> reviewsDto = new HashSet<>();

    public TrailDto(Trail trail) {
        if (trail.getId() != null) {
            this.id = trail.getId();
        }
        if (trail.getName() != null) {
            this.name = trail.getName();
        }
        if (trail.getAlias() != null) {
            this.alias = trail.getAlias();
        }
        if (trail.getImage() != null) {
            this.image = trail.getImage();
        }
    }
}
