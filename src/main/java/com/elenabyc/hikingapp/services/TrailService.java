package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.entities.Trail;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TrailService {
    @Transactional
    Trail addTrail(TrailDto trailDto);
    @Transactional
    List<String> saveTrail(TrailDto trailDto, Long userId);
    Optional<TrailDto> getTrailById(Long trailId);
    List<TrailDto> getTrailsByLocationName(String city);
    JsonNode getTrailDetailsByName(String name);


}
