package com.elenabyc.hikingapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@Data
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

//    public User(UserDto userDto) {
//        if (userDto.getUsername() != null) {
//            this.username = userDto.getUsername();
//        }
//        if (userDto.getPassword() != null) {
//            this.password = userDto.getPassword();
//        }
//    }
}

