package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.Coordinates;
import com.elenabyc.hikingapp.dtos.TrailDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class YelpAPIServiceImpl implements YelpAPIService {
    @Value("${yelp.dev-key}")
    private String YELP_DEV_KEY;

    @Override
    public List<TrailDto> getTrailsByLocationName(String city) {
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
                trailDto.setYelpAlias(element.get("alias").asText());
                trailDto.setYelpRating(element.get("rating").asDouble());
                trailDto.setYelpReviewCount(element.get("review_count").asInt());
                trailDto.setCoordinates(new Coordinates(
                        element.get("coordinates").get("latitude").asDouble(),
                        element.get("coordinates").get("longitude").asDouble()));
                trailDto.setImage(element.get("image_url").asText());
                list.add(trailDto);
            }
            System.out.println(list);
            return list;
//            return jsonNode;
        } catch (IOException e) {
            return null;
        }
    }
}
