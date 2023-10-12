package com.elenabyc.hikingapp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    String uploadPicture(MultipartFile file);
}
