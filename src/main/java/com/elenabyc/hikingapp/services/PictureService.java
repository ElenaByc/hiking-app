package com.elenabyc.hikingapp.services;

import com.elenabyc.hikingapp.dtos.PictureDto;

import java.util.List;

public interface PictureService {
    List<PictureDto> getAllPicturesByUserId(long userId);

    List<PictureDto> getAllPicturesByTrailId(long trailId);

    List<String> addPicture(PictureDto pictureDto, long userId);
}
