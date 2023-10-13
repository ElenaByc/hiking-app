package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.PictureDto;
import com.elenabyc.hikingapp.dtos.TrailDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureService {
    List<PictureDto> getAllPicturesByUserId(long userId);

    List<PictureDto> getAllPicturesByTrailId(long trailId);

    List<String> addPicture(TrailDto trailDto, long userId, MultipartFile file);

    List<String> deletePictureById(long pictureId);
}
