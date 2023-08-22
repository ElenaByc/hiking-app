package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.entities.Trail;
import com.elenabyc.hikingapp.repositories.TrailRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TrailServiceImpl implements TrailService {
    @Autowired
    private TrailRepository trailRepository;

    @Value("${yelp.dev-key}")
    private String YELP_DEV_KEY;

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
    public JsonNode getTrailsByLocationName(String city) {
        OkHttpClient client = new OkHttpClient();
        String yelpHikingUrl = String.format(
                "https://api.yelp.com/v3/businesses/search?location=%s&categories=hiking&sort_by=best_match&limit=50",
                city);
        System.out.println(yelpHikingUrl);
        Request request = new Request.Builder()
                .url(yelpHikingUrl)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + YELP_DEV_KEY)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("response code: " + response.code());
            String responseString = response.body().string();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseString);
            System.out.println(jsonNode.get("businesses"));
            System.out.println(jsonNode.get("businesses").size());
            List<TrailDto> list = new ArrayList<>();
            for (JsonNode element : jsonNode.get("businesses")) {
                TrailDto trailDto = new TrailDto();
                trailDto.setName(element.get("name").asText());
                trailDto.setAlias(element.get("alias").asText());
                trailDto.setImage(element.get("image_url").asText());
                list.add(trailDto);
            }
            System.out.println(list);

            return jsonNode;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Optional<TrailDto> getTrailById(Long trailId) {
//        yelp service
        Optional<Trail> trailOptional = trailRepository.findById(trailId);
        return trailOptional.map(TrailDto::new);
    }
}
