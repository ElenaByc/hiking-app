package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.entities.Trail;
import com.elenabyc.hikingapp.entities.User;
import com.elenabyc.hikingapp.repositories.TrailRepository;
import com.elenabyc.hikingapp.repositories.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private YelpAPIService yelpAPIService;

    @Autowired
    private GoogleAPIService googleAPIService;

    @Override
    @Transactional
    public Trail addTrail(TrailDto trailDto) {
        Optional<Trail> trailOptional = trailRepository.findByYelpAlias(trailDto.getYelpAlias());
        if (trailOptional.isPresent()) {
            return trailOptional.get();
        } else {
            Trail trail = new Trail(trailDto);
            trailRepository.saveAndFlush(trail);
            return trail;
        }
    }

    @Override
    public List<String> saveTrail(TrailDto trailDto, Long userId) {
        List<String> response = new ArrayList<>();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Trail trail = addTrail(trailDto);
            User user = userOptional.get();
            System.out.println(user);
            user.addTrail(trail);
            System.out.println(user);
            userRepository.saveAndFlush(user);
            response.add("Trail with id = " + trail.getId() + " was added to the user's Saved Trails list");
        } else {
            response.add("User with id = " + userId + " was not found");
            return response;
        }
        return response;
    }

    @Override
    public List<TrailDto> getTrailsByLocationName(String city) {
        List<TrailDto> searchResultList = yelpAPIService.getTrailsByLocationName(city);
        for (TrailDto trailDto : searchResultList) {
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
