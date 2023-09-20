package com.elenabyc.hikingapp.controllers;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.dtos.UserDto;
import com.elenabyc.hikingapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public List<String> addUser(@RequestBody UserDto userDto) {
        String passHash = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(passHash);
        return userService.addUser(userDto);
    }

    @PostMapping("/login")
    public List<String> userLogin(@RequestBody UserDto userDto) {
        return userService.userLogin(userDto);
    }

    @GetMapping("/saved/{userId}")
    public Set<TrailDto> getSavedTrailsByUserId(@PathVariable long userId ) {
        return userService.getSavedTrailsByUserId(userId);
    }
}
