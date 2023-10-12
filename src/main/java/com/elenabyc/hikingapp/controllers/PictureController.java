package com.elenabyc.hikingapp.controllers;

import com.elenabyc.hikingapp.dtos.PictureDto;
import com.elenabyc.hikingapp.services.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<String> addPicture(@RequestBody PictureDto pictureDto, @PathVariable long userId) {
        return pictureService.addPicture(pictureDto, userId);
    }
}
