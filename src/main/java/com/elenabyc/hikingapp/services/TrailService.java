package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TrailService {
    @Transactional
    List<String> addTrail(TrailDto trailDto);
}
