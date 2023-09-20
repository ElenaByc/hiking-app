package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.dtos.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface UserService {
    @Transactional
    List<String> addUser(UserDto userDto);

    List<String> userLogin(UserDto userDto);

    Set<TrailDto> getSavedTrailsByUserId(long userId);

    @Transactional
    List<String> removeTrail(long userId, long trailId);
}
