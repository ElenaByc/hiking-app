package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.dtos.UserDto;
import com.elenabyc.hikingapp.entities.User;
import com.elenabyc.hikingapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TrailService trailService;

    @Override
    @Transactional
    public List<String> addUser(UserDto userDto) {
        List<String> response = new ArrayList<>();
        Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail());
        if (userOptional.isPresent()) {
            response.add("The email has already been taken");
            return response;
        }
        User user = new User(userDto);
        userRepository.saveAndFlush(user);
        response.add("User added successfully");
        response.add("User id = " + user.getId());
        response.add("http://localhost:8080/login.html");
        return response;
    }

    @Override
    public List<String> userLogin(UserDto userDto) {
        List<String> response = new ArrayList<>();
        Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail().toLowerCase());
        if (userOptional.isPresent()) {
            if (passwordEncoder.matches(userDto.getPassword(), userOptional.get().getPassword())) {
                response.add("http://localhost:8080/index.html");
                response.add(String.valueOf(userOptional.get().getId()));
                response.add(String.valueOf(userOptional.get().getUsername()));
            } else {
                response.add("Username or password incorrect");
            }
        } else {
            response.add("Username or password incorrect");
        }
        return response;
    }

    @Override
    public Set<TrailDto> getSavedTrailsByUserId(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserDto userDto = new UserDto(userOptional.get());
            Set<TrailDto> savedTrails = userDto.getSavedTrailsDto();
            for (TrailDto trailDto : savedTrails) {
                trailService.getTrailRatings(trailDto);
            }
            return savedTrails;
        }
        return null;
    }

    @Override
    @Transactional
    public List<String> removeTrail(long userId, long trailId) {
        List<String> response = new ArrayList<>();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.removeTrail(trailId);
            response.add("Trail with id = " + trailId + " was removed from the user's Saved Trails List");
        } else {
            response.add("User with id = " + userId + " was not found");
        }
        return response;
    }
}
