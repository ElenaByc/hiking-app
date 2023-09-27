package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.fasterxml.jackson.databind.JsonNode;

public interface GoogleAPIService {
    void getTrailGooglePlacesData(TrailDto trailDto);

    String getImageByReference(String imgRef);

    JsonNode getTrailBasicDetails(String name);

    JsonNode getTrailDetailsByPlaceId(String googlePlaceId);

    void getTrailDetails(TrailDto trailDto);
}
