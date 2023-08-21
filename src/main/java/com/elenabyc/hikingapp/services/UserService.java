package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    @Transactional
    List<String> addUser(UserDto userDto);
    List<String> userLogin(UserDto userDto);
}
