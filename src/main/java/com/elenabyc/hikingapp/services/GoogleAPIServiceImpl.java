package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.*;
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
        JsonNode googlePlaceResponse;
        if (trailDto.getGooglePlaceId() != null) {
            // get Google Data from Google Place Details API
            googlePlaceResponse = getTrailDetailsByPlaceId(trailDto.getGooglePlaceId());
            if (googlePlaceResponse.get("result") != null) {
                if (googlePlaceResponse.get("result").get("rating") != null) {
                    trailDto.setGoogleRating(googlePlaceResponse.get("result").get("rating").asDouble());
                }
                if (googlePlaceResponse.get("result").get("user_ratings_total") != null) {
                    trailDto.setGoogleReviewCount(googlePlaceResponse.get("result").get("user_ratings_total").asInt());
                }
            }
        }
        if (trailDto.getGooglePlaceId() == null || trailDto.getGoogleRating() == 0) {
            // get Google Data from Google Place Search API
            // googlePlaceSearchResponse = getTrailBasicDetails(trailDto.getName());
            googlePlaceResponse = searchPlace(trailDto);
            if (googlePlaceResponse != null) {
                trailDto.setGooglePlaceId(googlePlaceResponse.get("place_id").asText());
                double latitude = googlePlaceResponse.get("geometry").get("location").get("lat").asDouble();
                double longitude = googlePlaceResponse.get("geometry").get("location").get("lng").asDouble();
                trailDto.setGoogleCoordinates(new Coordinates(latitude, longitude));
                if (googlePlaceResponse.get("rating") != null) {
                    trailDto.setGoogleRating(googlePlaceResponse.get("rating").asDouble());
                }
                if (googlePlaceResponse.get("user_ratings_total") != null) {
                    trailDto.setGoogleReviewCount(googlePlaceResponse.get("user_ratings_total").asInt());
                }
                if (googlePlaceResponse.get("photos") != null &&
                        googlePlaceResponse.get("photos").size() > 0) {
                    String imgRef = googlePlaceResponse.get("photos").get(0).get("photo_reference").asText();
                    trailDto.setImage(getImageByReference(imgRef));
                }
            }
        }
    }


    private JsonNode searchPlace(TrailDto trailDto) {
        JsonNode googlePlaceSearchResponse = getTrailBasicDetails(trailDto.getYelpAlias());
        if (googlePlaceSearchResponse == null) {
            System.out.println("!!!!!!! NO RESPONSE FROM GOOGLE API");
            return null;
        }
        double latitude;
        double longitude;
        for (JsonNode element : googlePlaceSearchResponse.get("candidates")) {
            latitude = element.get("geometry").get("location").get("lat").asDouble();
            longitude = element.get("geometry").get("location").get("lng").asDouble();

            if (Math.abs(latitude - trailDto.getCoordinates().getLatitude()) < 1 &&
                    Math.abs(longitude - trailDto.getCoordinates().getLongitude()) < 1) {
                // correct trail was found
                return element;
            }
        }
        return null;
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
//                "formatted_address%2C" +
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
        OkHttpClient client = new OkHttpClient();
        String fields = "?fields=" +
                "rating%2C" +
                "user_ratings_total";
        String placeId = "&place_id=" + googlePlaceId;
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
            return objectMapper.readTree(responseString);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void getTrailDetails(TrailDto trailDto) {
        OkHttpClient client = new OkHttpClient();
        String fields = "?fields=" +
                "website%2C" +
                "url%2C" +
                "photos%2C" +
                "reviews";
        String placeId = "&place_id=" + trailDto.getGooglePlaceId();
        String key = "&key=" + GOOGLE_DEV_KEY;
        int beginIdx;
        int endIdx;

        String googlePlacesDetailsUrl = "https://maps.googleapis.com/maps/api/place/details/json" +
                fields +
                placeId +
                key;
        Request request = new Request.Builder()
                .url(googlePlacesDetailsUrl)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode placeDetailsResponse = objectMapper.readTree(responseString);
            JsonNode result = placeDetailsResponse.get("result");
            if (result != null) {
                if (result.get("url") != null) {
                    trailDto.setGoogleLink(result.get("url").asText());
                }
                if (result.get("website") != null) {
                    trailDto.setWebsite(result.get("website").asText());
                }
                if (result.get("reviews") != null) {
                    for (JsonNode review : result.get("reviews")) {
                        ReviewDto reviewDto = new ReviewDto();
                        UserDto userDto = new UserDto();
                        userDto.setUsername(review.get("author_name").asText());
                        reviewDto.setUserDto(userDto);
                        reviewDto.setRating(review.get("rating").asInt());
                        reviewDto.setBody(review.get("text").asText());

                        reviewDto.setDate(new java.text.SimpleDateFormat("MM/dd/yyyy")
                                .format(new java.util.Date(review.get("time").asLong() * 1000)));
                        reviewDto.setSource("Google Maps");

                        trailDto.getGoogleReviews().add(reviewDto);
                    }
                }
                if (result.get("photos") != null) {
                    for (JsonNode photo : result.get("photos")) {
                        PictureDto pictureDto = new PictureDto();
                        UserDto userDto = new UserDto();
                        String contributor = photo.get("html_attributions").get(0).asText();
                        beginIdx = contributor.indexOf(">");
                        endIdx = contributor.indexOf("</a>");
                        userDto.setUsername(contributor.substring(beginIdx + 1, endIdx));
                        pictureDto.setUserDto(userDto);
                        String imgRef = photo.get("photo_reference").asText();
                        pictureDto.setUrl(getImageByReference(imgRef));
                        pictureDto.setSource("Google Maps");
                        trailDto.getGooglePictures().add(pictureDto);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("getTrailDetails: " + e);
        }
    }

    @Override
    public void getTrailRating(TrailDto trailDto) {
        JsonNode placeDetailsResponse = getTrailDetailsByPlaceId(trailDto.getGooglePlaceId());
        if (placeDetailsResponse != null) {
            if (placeDetailsResponse.get("result") != null) {
                if (placeDetailsResponse.get("result").get("rating") != null) {
                    trailDto.setGoogleRating(placeDetailsResponse.get("result").get("rating").asDouble());
                }
                if (placeDetailsResponse.get("result").get("user_ratings_total") != null) {
                    trailDto.setGoogleReviewCount(placeDetailsResponse.get("result").get("user_ratings_total").asInt());
                }
            }
        }
        if (trailDto.getGoogleRating() == 0 && trailDto.getCoordinates() != null) {
            JsonNode placeSearchResponse = searchPlace(trailDto);
            if (placeSearchResponse != null) {
                if (placeSearchResponse.get("rating") != null) {
                    trailDto.setGoogleRating(placeSearchResponse.get("rating").asDouble());
                }
                if (placeSearchResponse.get("user_ratings_total") != null) {
                    trailDto.setGoogleReviewCount(placeSearchResponse.get("user_ratings_total").asInt());
                }
            }
        }
    }
}
