package com.spring.springbootapplication.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Controller
public class ProfileController {
    private final Cloudinary cloudinary;

    public ProfileController(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttrs) {
        if (file.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "ファイルが選択されていません");
            return "redirect:/profile";
        }

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "myprotfolio",
                            "public_id", "defaultThumbnail", // これで defaultThumbnail.avif 固定
                            "overwrite", true,
                            "format", "avif"));

            String imageUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            // 画面表示・通知用（redirect後に使える）
            redirectAttrs.addFlashAttribute("imageUrl", imageUrl);
            redirectAttrs.addFlashAttribute("message", "アップロードしました");
            // DBに public_id を保存したい場合はここで保存処理を

        } catch (IOException e) {
            redirectAttrs.addFlashAttribute("error", "アップロードに失敗しました: " + e.getMessage());
        }

        return "redirect:/profile";
    }
}