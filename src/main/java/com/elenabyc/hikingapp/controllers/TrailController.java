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

    @PostMapping("/add")
    public List<String> addTrail(@RequestBody TrailDto trailDto) {
        return trailService.addTrail(trailDto);
    }

    @GetMapping("/{trailId}")
    public Optional<TrailDto> getTrailById(@PathVariable Long trailId) {
        return trailService.getTrailById(trailId);
    }

    @GetMapping("/location/{city}")
    public JsonNode getTrailsByLocationName(@PathVariable String city) {
        return trailService.getTrailsByLocationName(city);
    }

}