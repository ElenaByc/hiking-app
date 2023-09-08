package com.elenabyc.hikingapp.entities;

import com.elenabyc.hikingapp.dtos.UserDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
//@Data
@Setter
@Getter
//@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonManagedReference
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "saved_trails",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "trail_id"))
    private Set<Trail> savedTrails = new HashSet<>();

    @Override
    public String toString() {
        return "User:" + + this.id + " " + this.username + "\n Saved Trails:" +  this.savedTrails;
    }
    public void addTrail(Trail trail) {
        this.savedTrails.add(trail);
        trail.getUsers().add(this);
    }

    public void removeTrail(long trailId) {
        Trail trail = this.savedTrails.stream()
                .filter(t -> t.getId() == trailId)
                .findFirst().orElse(null);
        if (trail != null) {
            this.savedTrails.remove(trail);
            trail.getUsers().remove(this);
        }
    }

    public User(UserDto userDto) {
        if (userDto.getUsername() != null) {
            this.username = userDto.getUsername();
        }
        if (userDto.getEmail() != null) {
            this.email = userDto.getEmail().toLowerCase();
        }
        if (userDto.getPassword() != null) {
            this.password = userDto.getPassword();
        }
    }
}

