package com.elenabyc.hikingapp.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadPicture(MultipartFile file) {
        try {
//            Map uploadResult = cloudinary.uploader().upload(
//                    "https://cloudinary-devs.github.io/cld-docs-assets/assets/images/coffee_cup.jpg",
//                    ObjectUtils.emptyMap());
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            System.out.println("uploadResult: " + uploadResult);
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

}
