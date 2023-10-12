package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.PictureDto;
import com.elenabyc.hikingapp.dtos.TrailDto;
import com.elenabyc.hikingapp.entities.Picture;
import com.elenabyc.hikingapp.entities.Trail;
import com.elenabyc.hikingapp.entities.User;
import com.elenabyc.hikingapp.repositories.PictureRepository;
import com.elenabyc.hikingapp.repositories.TrailRepository;
import com.elenabyc.hikingapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TrailRepository trailRepository;
    @Autowired
    private TrailService trailService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<PictureDto> getAllPicturesByUserId(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            List<Picture> pictureList = pictureRepository.findAllByUserEquals(userOptional.get());
            return pictureList.stream().map(PictureDto::new).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<PictureDto> getAllPicturesByTrailId(long trailId) {
        Optional<Trail> trailOptional = trailRepository.findById(trailId);
        if (trailOptional.isPresent()) {
            List<Picture> pictureList = pictureRepository.findAllByTrailEquals(trailOptional.get());
            return pictureList.stream().map(PictureDto::new).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> addPicture(TrailDto trailDto, long userId, MultipartFile file) {
        List<String> response = new ArrayList<>();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            response.add("Error: there is no user with id = " + userId);
            return response;
        }
        Trail trail = trailService.addTrail(trailDto);
        String pictureUrl = cloudinaryService.uploadPicture(file);
        Picture picture = new Picture();
        picture.setUrl(pictureUrl);
        userOptional.ifPresent(picture::setUser);
        picture.setTrail(trail);
        pictureRepository.saveAndFlush(picture);
        response.add("Picture with id = " + picture.getId() + " added successfully");
        response.add(pictureUrl);
        return response;
    }
}
