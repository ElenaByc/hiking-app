package com.elenabyc.hikingapp.controllers;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.services.TrailService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trails")
public class TrailController {
    @Autowired
    private TrailService trailService;

    @PostMapping("/save/{userId}")
    public List<String> saveTrail(@RequestBody TrailDto trailDto, @PathVariable Long userId) {
        return trailService.saveTrail(trailDto, userId);
    }

    @GetMapping("/{trailId}")
    public Optional<TrailDto> getTrailById(@PathVariable Long trailId) {
        return trailService.getTrailById(trailId);
    }

    @GetMapping("/location/{city}/{userId}")
    public List<TrailDto> getTrailsByLocationName(@PathVariable String city, @PathVariable long userId ) {
        return trailService.getTrailsByLocationName(city, userId);
    }

    @GetMapping("/details/{yelpAlias}/{googlePlaceId}")
    public TrailDto getTrailDetails(@PathVariable String yelpAlias, @PathVariable String googlePlaceId) {
        return trailService.getTrailDetails(yelpAlias, googlePlaceId);
    }
}
