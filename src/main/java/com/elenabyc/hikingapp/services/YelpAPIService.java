package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;

import java.util.List;

public interface YelpAPIService {
    List<TrailDto> getTrailsByLocationName(String city);

    void getTrailReviews(TrailDto trailDto);

    void getTrailPictures(TrailDto trailDto);

    void getTrailRating(TrailDto trailDto);
}
