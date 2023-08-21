package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TrailService {
    @Transactional
    List<String> addTrail(TrailDto trailDto);

    Optional<TrailDto> getTrailById(Long trailId);
}
