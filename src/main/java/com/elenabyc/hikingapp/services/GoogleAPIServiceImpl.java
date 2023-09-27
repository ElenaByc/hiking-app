package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.Coordinates;
import com.elenabyc.hikingapp.dtos.TrailDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GoogleAPIServiceImpl implements GoogleAPIService {
    @Value("${google.dev-key}")
    private String GOOGLE_DEV_KEY;

    @Override
    public void getTrailGooglePlacesData(TrailDto trailDto) {
        JsonNode googlePlaceSearchResponse;
        if (trailDto.getGooglePlaceId() != null) {
            //TODO: get Google Data from Google Place Details API
        } else { // get Google Data from Google Place Search API
//            googlePlacesAPIResponse = getTrailBasicDetails(trailDto.getName());
            googlePlaceSearchResponse = getTrailBasicDetails(trailDto.getYelpAlias());
            if (googlePlaceSearchResponse == null) {
                System.out.println("!!!!!!! NO RESPONSE FROM GOOGLE API");
                return;
            }
            double latitude;
            double longitude;
            for (JsonNode element : googlePlaceSearchResponse.get("candidates")) {
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
        String maxWidth = "?maxwidth=1600";
        String key = "&key=" + GOOGLE_DEV_KEY;

        String googlePlacePhotosUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                maxWidth +
                "&photo_reference=" + imgRef +
                key;
        Request request = new Request.Builder()
                .url(googlePlacePhotosUrl)
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
    public JsonNode getTrailBasicDetails(String trailName) {
        OkHttpClient client = new OkHttpClient();
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

        String googleFindPlaceUrl = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json" +
                fields +
                input +
                key;
        Request request = new Request.Builder()
                .url(googleFindPlaceUrl)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(responseString);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public JsonNode getTrailDetailsByPlaceId(String googlePlaceId) {
        return null;
    }

    @Override
    public void getTrailDetails(TrailDto trailDto) {
        OkHttpClient client = new OkHttpClient();
        String fields = "?fields=" +
                "website%2C" +
                "url%2C" +
                "photo%2C" +
                "reviews";
        String placeId = "&place_id=" + trailDto.getGooglePlaceId();
        String key = "&key=" + GOOGLE_DEV_KEY;

        String googlePlaceDetailsUrl = "https://maps.googleapis.com/maps/api/place/details/json" +
                fields +
                placeId +
                key;
        Request request = new Request.Builder()
                .url(googlePlaceDetailsUrl)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode placeDetailsResponse = objectMapper.readTree(responseString);
            if (placeDetailsResponse.get("result") != null) {
                if (placeDetailsResponse.get("result").get("url") != null) {
                    trailDto.setGoogleLink(placeDetailsResponse.get("result").get("url").asText());
                }
                if (placeDetailsResponse.get("result").get("website") != null) {
                    trailDto.setWebsite(placeDetailsResponse.get("result").get("website").asText());
                }
            }
        } catch (IOException e) {
            System.out.println("getTrailDetails: " + e);
        }
    }
}
