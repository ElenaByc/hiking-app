package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface YelpAPIService {
    List<TrailDto> getTrailsByLocationName(String city);
}
