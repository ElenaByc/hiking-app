package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.entities.Trail;
import com.elenabyc.hikingapp.entities.User;
import com.elenabyc.hikingapp.repositories.TrailRepository;
import com.elenabyc.hikingapp.repositories.UserRepository;
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
    public List<String> saveTrail(TrailDto trailDto, long userId) {
        List<String> response = new ArrayList<>();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Trail trail = addTrail(trailDto);
            User user = userOptional.get();
            user.addTrail(trail);
            userRepository.saveAndFlush(user);
            response.add("Trail with id = " + trail.getId() + " was added to the user's Saved Trails list");
        } else {
            response.add("User with id = " + userId + " was not found");
        }
        return response;
    }

    @Override
    public List<TrailDto> getTrailsByLocationName(String city, long userId) {
        List<TrailDto> searchResultList = yelpAPIService.getTrailsByLocationName(city);
        List<TrailDto> searchResultListFinal = new ArrayList<>();
        Optional<User> userOptional = userRepository.findById(userId);
        for (TrailDto trailDto : searchResultList) {
            Optional<Trail> trailOptional = trailRepository.findByYelpAlias(trailDto.getYelpAlias());
            if (userOptional.isPresent() && trailOptional.isPresent()) {
                User user = userOptional.get();
                Trail trail = trailOptional.get();
                if (user.getSavedTrails().contains(trail)) {
                    trailDto.setSaved(true);
                }
            }
            googleAPIService.getTrailGooglePlacesData(trailDto);
            // remove trails without Google Places data from search result
            // remove -gym-
            if (trailDto.getGooglePlaceId() != null && !trailDto.getYelpAlias().contains("-gym-")) {
                searchResultListFinal.add(trailDto);
            }
        }
        return searchResultListFinal;
    }

    @Override
    public TrailDto getTrailDetails(String yelpAlias, String googlePlaceId) {
        Optional<Trail> trailOptional = trailRepository.findByYelpAlias(yelpAlias);
        TrailDto trailDto;
        if (trailOptional.isPresent()) {
            trailDto = new TrailDto(trailOptional.get());
        } else {
            trailDto = new TrailDto();
            trailDto.setYelpAlias(yelpAlias);
            trailDto.setGooglePlaceId(googlePlaceId);
        }

        // get pictures, reviews, link on trails' Google Map page, website
        googleAPIService.getTrailDetails(trailDto);


        return trailDto;
    }

    @Override
    public Optional<TrailDto> getTrailById(Long trailId) {
        Optional<Trail> trailOptional = trailRepository.findById(trailId);
        return trailOptional.map(TrailDto::new);
    }
}
