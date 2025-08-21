package com.spring.springbootapplication.web;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileForm {
    @NotNull(message = "ユーザーIDは必須です")
    private Long id; // ← フィールド名とゲッター/セッターを id に統一

    @Size(min = 50, max = 200, message = "自己紹介は50文字以上、200文字以下で入力してください")
    private String biography;

    // private MultipartFile thumbnail;
    // Cloudinary の public_id を受け取る
    @Column(name = "thumbnail_public_id")
    private String thumbnailPublicId;
    private String thumbnailName;

}