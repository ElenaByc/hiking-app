package com.elenabyc.hikingapp.services;

import com.fasterxml.jackson.databind.JsonNode;

public interface YelpAPIService {
    JsonNode getTrailsByLocationName(String city);
}
