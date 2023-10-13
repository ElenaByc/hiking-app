package com.elenabyc.hikingapp.controllers;

import com.elenabyc.hikingapp.dtos.PictureDto;
import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.services.PictureService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/pictures")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @GetMapping("/user/{userId}")
    public List<PictureDto> getAllPicturesByUserId(@PathVariable long userId) {
        return pictureService.getAllPicturesByUserId(userId);
    }

    @GetMapping("/trail/{trailId}")
    public List<PictureDto> getAllPicturesByTrailId(@PathVariable long trailId) {
        return pictureService.getAllPicturesByTrailId(trailId);
    }

    @PostMapping(value = "/upload/{userId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})

    public List<String> addPicture(@RequestPart("trail") String trailStr,
                                   @PathVariable long userId,
                                   @RequestPart("file") MultipartFile file) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println("TrailStr: " + trailStr);
            TrailDto trailDto = objectMapper.readValue(trailStr, TrailDto.class);
            return pictureService.addPicture(trailDto, userId, file);
        } catch (IOException e) {
            List<String> result = new ArrayList<>();
            result.add("Error: cannot convert trailStr to TrailDto");
            return result;
        }
    }

    @PostMapping("/delete/{pictureId}")
    public List<String> deletePictureById(@PathVariable long pictureId) {
        return pictureService.deletePictureById(pictureId);
    }
}
