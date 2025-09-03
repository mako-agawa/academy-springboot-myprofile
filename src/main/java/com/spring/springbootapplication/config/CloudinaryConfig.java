// // src/main/java/.../config/CloudinaryConfig.java
// package com.spring.springbootapplication.config;

// import com.cloudinary.Cloudinary;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class CloudinaryConfig {
//     @Bean
//     Cloudinary cloudinary(
//             // ← 空でも起動できるようにデフォルト空文字を入れる
//             @Value("${cloudinary.url:}") String cloudinaryUrl,
//             @Value("${cloudinary.cloud-name:}") String cloudName) {

//         if (cloudinaryUrl != null && !cloudinaryUrl.isBlank() && !cloudinaryUrl.contains("${")) {

//             return new Cloudinary(cloudinaryUrl);
//         }

//         if (cloudName == null || cloudName.isBlank()) {
//             // どっちも無いなら、わかりやすいメッセージで失敗させる
//             throw new IllegalStateException(
//                     "Cloudinary configuration missing. Set CLOUDINARY_URL or cloudinary.url in properties " +
//                             "(and/or CLOUDINARY_CLOUD_NAME for URL builder).");
//         }
//         // パターン2: URLが無い場合は cloud_name のみで（署名不要の配信URL生成用）
//         return new Cloudinary(Map.of("cloud_name", cloudName));
//     }

// }