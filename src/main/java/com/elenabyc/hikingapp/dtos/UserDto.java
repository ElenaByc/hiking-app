package com.elenabyc.hikingapp.dtos;

import com.elenabyc.hikingapp.entities.Trail;
import com.elenabyc.hikingapp.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<TrailDto> savedTrailsDto = new HashSet<>();

    public UserDto(User user) {
        if (user.getId() != null) {
            this.id = user.getId();
        }
        if (user.getUsername() != null) {
            this.username = user.getUsername();
        }
        if (user.getEmail() != null) {
            this.email = user.getEmail();
        }
        if (user.getPassword() != null) {
            this.password = user.getPassword();
        }
        if (user.getSavedTrails().size() > 0) {
            for (Trail trail : user.getSavedTrails()) {
                this.savedTrailsDto.add(new TrailDto(trail));
            }
        }
    }
}
