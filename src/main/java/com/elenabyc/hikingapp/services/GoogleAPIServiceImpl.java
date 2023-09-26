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
public class GoogleAPIServiceImpl implements GoogleAPIService {
    @Value("${google.dev-key}")
    private String GOOGLE_DEV_KEY;

    @Override
    public void getTrailGooglePlacesData(TrailDto trailDto) {
        JsonNode googlePlacesAPIResponse;
        if (trailDto.getGooglePlaceId() != null) {
            //TODO: get Google Data from Google Place Details API
        } else { // get Google Data from Google Place Search Place API
//            googlePlacesAPIResponse = getTrailDetailsByName(trailDto.getName());
            googlePlacesAPIResponse = getTrailDetailsByName(trailDto.getYelpAlias());
            if (googlePlacesAPIResponse == null) {
                System.out.println("!!!!!!! NO RESPONSE FROM GOOGLE API");
                return;
            }
//            System.out.println(googlePlacesAPIResponse.get("candidates"));
//            System.out.println(googlePlacesAPIResponse.get("candidates").size());
            double latitude;
            double longitude;
            for (JsonNode element : googlePlacesAPIResponse.get("candidates")) {
                latitude = element.get("geometry").get("location").get("lat").asDouble();
                longitude = element.get("geometry").get("location").get("lng").asDouble();

                if (Math.abs(latitude - trailDto.getCoordinates().getLatitude()) < 1 &&
                        Math.abs(longitude - trailDto.getCoordinates().getLongitude()) < 1) {
                    // correct trail was found
                    trailDto.setGooglePlaceId(element.get("place_id").asText());
                    trailDto.setGoogleCoordinates(new Coordinates(latitude, longitude));
                    if (element.get("rating") != null) {
                        trailDto.setGoogleRating(element.get("rating").asDouble());
                    }
                    if (element.get("user_ratings_total") != null) {
                        trailDto.setGoogleReviewCount(element.get("user_ratings_total").asInt());
                    }
//                    if (element.get("formatted_address") != null) {
//                        trailDto.setAddress(element.get("formatted_address").asText());
//                    }
//                    if (trailDto.getImage() == null && element.get("photos") != null &&
                    if (element.get("photos") != null &&
                            element.get("photos").size() > 0) {
                        String imgRef = element.get("photos").get(0).get("photo_reference").asText();
                        trailDto.setImage(getImageByReference(imgRef));
                    }
                    return;
                }
            }
        }
    }

    @Override
    public String getImageByReference(String imgRef) {
        OkHttpClient client = new OkHttpClient();
        StringBuilder googlePlacePhotosUrl = new StringBuilder();
        String maxWidth = "?maxwidth=1600";
        String key = "&key=" + GOOGLE_DEV_KEY;

        googlePlacePhotosUrl.append("https://maps.googleapis.com/maps/api/place/photo");
        googlePlacePhotosUrl.append(maxWidth);
        googlePlacePhotosUrl.append("&photo_reference=" + imgRef);
        googlePlacePhotosUrl.append(key);
        Request request = new Request.Builder()
                .url(googlePlacePhotosUrl.toString())
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            String imageUrl = response.request().url().toString();
            response.body().close();
            return imageUrl;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public JsonNode getTrailDetailsByName(String trailName) {
        OkHttpClient client = new OkHttpClient();
        StringBuilder googleFindPlaceUrl = new StringBuilder();
        String input = "&input=" + trailName + "&inputtype=textquery";
        String fields = "?fields=" +
                "formatted_address%2C" +
                "name%2C" +
                "rating%2C" +
                "user_ratings_total%2C" +
                "geometry%2C" +
                "photo%2C" +
                "place_id";
        String key = "&key=" + GOOGLE_DEV_KEY;

        googleFindPlaceUrl.append("https://maps.googleapis.com/maps/api/place/findplacefromtext/json");
        googleFindPlaceUrl.append(fields);
        googleFindPlaceUrl.append(input);
        googleFindPlaceUrl.append(key);
        Request request = new Request.Builder()
                .url(googleFindPlaceUrl.toString())
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("Trail name: " + trailName);
            System.out.println("Google API response code: " + response.code());
            String responseString = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseString);
            System.out.println("Google API response status: " + jsonNode.get("status"));
            return jsonNode;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public JsonNode getTrailDetailsByPlaceId(String googlePlaceId) {
        return null;
    }
}
