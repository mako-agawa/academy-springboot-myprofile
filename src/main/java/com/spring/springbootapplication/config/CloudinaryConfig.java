package com.spring.springbootapplication.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.Url;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        // 環境変数 CLOUDINARY_URL=cloudinary://API_KEY:API_SECRET@CLOUD_NAME を自動読込
        return new Cloudinary();

    }

    public String avatarUrlTransformed(String publicId, Cloudinary cloudinary) {
        Url url = cloudinary.url()
                .resourceType("image")
                .transformation(new Transformation()
                        .width(300).crop("scale")
                        .fetchFormat("auto") // f_auto
                        .quality("auto")); // q_auto
        return url.generate(publicId);
    }
}