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

    @GetMapping("/location/{city}")
    public List<TrailDto> getTrailsByLocationName(@PathVariable String city) {
        return trailService.getTrailsByLocationName(city);
    }

    @GetMapping("/details/{name}")
    public JsonNode getTrailDetailsByName(@PathVariable String name) {
        return trailService.getTrailDetailsByName(name);
    }


}
