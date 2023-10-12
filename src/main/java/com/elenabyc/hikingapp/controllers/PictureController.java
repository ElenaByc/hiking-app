package com.elenabyc.hikingapp.controllers;

import com.elenabyc.hikingapp.dtos.PictureDto;
import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.services.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/upload/{userId}")
    // @RequestParam("TrailDto") TrailDto trailDto,
    public List<String> addPicture(@RequestParam("TrailDto") TrailDto trailDto,
                                   @PathVariable long userId,
                                   @RequestParam("file") MultipartFile file) {
//        TrailDto trailDto = new TrailDto();
//        trailDto.setName("Test Trail to add picture");
//        trailDto.setYelpAlias("test_trail_yelp_alias");
//        trailDto.setGooglePlaceId("test_trail_google_places_id");
//        trailDto.setAddress("Test Trail address");
        return pictureService.addPicture(trailDto, userId, file);
    }
}
