package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.entities.Trail;
import com.elenabyc.hikingapp.repositories.TrailRepository;
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

    @Override
    @Transactional
    public List<String> addTrail(TrailDto trailDto) {
        List<String> response = new ArrayList<>();
        Optional<Trail> trailOptional = trailRepository.findByAlias(trailDto.getAlias());
        if (trailOptional.isPresent()) {
            response.add("The trail is already in DB");
        } else {
            Trail trail = new Trail(trailDto);
            trailRepository.saveAndFlush(trail);
            response.add("The trail was added to DB");
            trailOptional = trailRepository.findByAlias(trailDto.getAlias());
        }
        // return the trail id
        trailOptional.ifPresent(trail -> response.add(String.valueOf(trail.getId())));
        return response;
    }

    @Override
    public List<TrailDto> getTrailsByLocationName(String city) { //}, category, distance, rank) {
        // Controller -> getAllTrail (location) ->
        // call YelpService.getTrailByLocationName(city); parse JSON response  -> dtos
        // categories = hiking
        // dtos for responses
        List<TrailDto> list = new ArrayList<>();
        return list;
    }

    @Override
    public Optional<TrailDto> getTrailById(Long trailId) {
//        yelp service
        Optional<Trail> trailOptional = trailRepository.findById(trailId);
        return trailOptional.map(TrailDto::new);
    }
}
