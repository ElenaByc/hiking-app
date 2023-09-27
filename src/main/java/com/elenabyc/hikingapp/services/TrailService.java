package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.entities.Trail;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TrailService {
    @Transactional
    Trail addTrail(TrailDto trailDto);
    @Transactional
    List<String> saveTrail(TrailDto trailDto, long userId);
    Optional<TrailDto> getTrailById(Long trailId);
    List<TrailDto> getTrailsByLocationName(String city, long userId);
    TrailDto getTrailDetails(String yelpAlias, String googlePlaceId);
    //    JsonNode getTrailByName(String name);
}
