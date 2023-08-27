package com.elenabyc.hikingapp.services;

import com.fasterxml.jackson.databind.JsonNode;

public interface GoogleAPIService {
    JsonNode getTrailDetailsByName(String name);
}
