package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.Coordinates;
import com.elenabyc.hikingapp.dtos.ReviewDto;
import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.dtos.UserDto;
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
        final int radius = 20000;  // The max value is 40,000 meters (about 25 miles)
        final int limit = 10;      // max = 50
        final String sortBy = "best_match"; // best_match, rating, review_count or distance
        OkHttpClient client = new OkHttpClient();
        String yelpHikingUrl = "https://api.yelp.com/v3/businesses/search?" +
                "location=" + city +
                "&radius=" + radius +
                "&categories=hiking" +
                "&sort_by=" + sortBy +
                "&limit=" + limit;

        Request request = new Request.Builder()
                .url(yelpHikingUrl)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + YELP_DEV_KEY)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseString);
            List<TrailDto> list = new ArrayList<>();
            for (JsonNode element : jsonNode.get("businesses")) {
                TrailDto trailDto = new TrailDto();
                trailDto.setName(element.get("name").asText());
                trailDto.setYelpAlias(element.get("alias").asText());
                trailDto.setYelpRating(element.get("rating").asDouble());
                trailDto.setYelpReviewCount(element.get("review_count").asInt());
                trailDto.setYelpLink(element.get("url").asText());
                trailDto.setCoordinates(new Coordinates(
                        element.get("coordinates").get("latitude").asDouble(),
                        element.get("coordinates").get("longitude").asDouble()));
                trailDto.setImage(element.get("image_url").asText());
                if (element.get("location") != null) {
                    if (element.get("location").get("display_address") != null) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < element.get("location").get("display_address").size(); i++) {
                            sb.append(element.get("location").get("display_address").get(i).asText());
                            if (i != element.get("location").get("display_address").size() - 1) {
                                sb.append(", ");
                            }
                        }
                        trailDto.setAddress(sb.toString());
                    }
                }
                list.add(trailDto);
            }
            return list;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void getTrailReviews(TrailDto trailDto) {


        OkHttpClient client = new OkHttpClient();
        String yelpReviewsUrl = "https://api.yelp.com/v3/businesses/" +
                trailDto.getYelpAlias() +
                "/reviews?limit=20&sort_by=yelp_sort";

        Request request = new Request.Builder()
                .url(yelpReviewsUrl)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + YELP_DEV_KEY)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseString);
            for (JsonNode review : jsonNode.get("reviews")) {
                ReviewDto reviewDto = new ReviewDto();
                UserDto userDto = new UserDto();
                userDto.setUsername(review.get("user").get("name").asText());
                reviewDto.setUserDto(userDto);
                reviewDto.setRating(review.get("rating").asInt());
                reviewDto.setBody(review.get("text").asText());
                reviewDto.setUrl(review.get("url").asText());
                String timeCreated = review.get("time_created").asText();
                reviewDto.setDate(timeCreated.substring(8, 10) + "/"
                        + timeCreated.substring(5, 7) + "/"
                        + timeCreated.substring(0, 4));
                reviewDto.setSource("Yelp");

                trailDto.getYelpReviews().add(reviewDto);
            }

        } catch (IOException e) {
            return;
        }

    }
}
