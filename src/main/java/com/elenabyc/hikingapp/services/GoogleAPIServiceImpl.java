package com.elenabyc.hikingapp.services;

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
    public JsonNode getTrailDetailsByName(String name) {
        OkHttpClient client = new OkHttpClient();
        StringBuilder googleFindPlaceUrl = new StringBuilder();
        String input = "&input=" + name + "&inputtype=textquery";
        String fields = "?fields=formatted_address%2Cname%2Crating%2Copening_hours%2Cgeometry%2Ctype%2Cphoto%2Cplace_id";
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
            System.out.println("Google API response code: " + response.code());
            String responseString = response.body().string();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseString);
            System.out.println(jsonNode.get("candidates"));
            System.out.println(jsonNode.get("candidates").size());
            List<TrailDto> list = new ArrayList<>();
            for (JsonNode element : jsonNode.get("candidates")) {
                TrailDto trailDto = new TrailDto();
                trailDto.setName(element.get("name").asText());
                trailDto.setGooglePlaceId(element.get("place_id").asText());
                if (element.get("rating") != null) {
                    trailDto.setGoogleRating(element.get("rating").asDouble());
                }
                if (element.get("photos") != null) {
                    trailDto.setImage(element.get("photos").get(0).get("photo_reference").asText());
                }
                list.add(trailDto);
            }
            System.out.println(list);

            return jsonNode;
        } catch (IOException e) {
            return null;
        }
    }
}
