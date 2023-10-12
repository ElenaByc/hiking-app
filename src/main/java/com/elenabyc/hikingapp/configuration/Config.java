package com.elenabyc.hikingapp.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${cloudinary.cloud-name}")
    private String CLOUDINARY_CLOUD_NAME;
    @Value("${cloudinary.api-key}")
    private String CLOUDINARY_API_KEY;
    @Value("${cloudinary.api-secret}")
    private String CLOUDINARY_API_SECRET;

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", CLOUDINARY_CLOUD_NAME);
        config.put("api_key", CLOUDINARY_API_KEY);
        config.put("api_secret", CLOUDINARY_API_SECRET);
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}
