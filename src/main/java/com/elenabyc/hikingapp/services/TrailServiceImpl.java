package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.entities.Trail;
import com.elenabyc.hikingapp.repositories.TrailRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TrailServiceImpl implements TrailService {
    @Autowired
    private TrailRepository trailRepository;

    @Autowired
    private YelpAPIService yelpAPIService;

    @Autowired
    private GoogleAPIService googleAPIService;

    @Override
    @Transactional
    public List<String> addTrail(TrailDto trailDto) {
        List<String> response = new ArrayList<>();
        Optional<Trail> trailOptional = trailRepository.findByYelpAlias(trailDto.getYelpAlias());
        if (trailOptional.isPresent()) {
            response.add("The trail is already in DB");
        } else {
            Trail trail = new Trail(trailDto);
            trailRepository.saveAndFlush(trail);
            response.add("The trail was added to DB");
            trailOptional = trailRepository.findByYelpAlias(trailDto.getYelpAlias());
        }
        // return the trail id
        trailOptional.ifPresent(trail -> response.add(String.valueOf(trail.getId())));
        return response;
    }

    @Override
    public List<TrailDto> getTrailsByLocationName(String city) {
        List<TrailDto> searchResultList = yelpAPIService.getTrailsByLocationName(city);
        for (TrailDto trailDto: searchResultList) {
            // TODO check if this trail in DB and if so get its GooglePlacesId
            googleAPIService.getTrailGooglePlacesData(trailDto);
        }
        return searchResultList;
    }

    @Override
    public JsonNode getTrailDetailsByName(String name) {
        return googleAPIService.getTrailDetailsByName(name);
    }

    @Override
    public Optional<TrailDto> getTrailById(Long trailId) {
        Optional<Trail> trailOptional = trailRepository.findById(trailId);
        return trailOptional.map(TrailDto::new);
    }
}
