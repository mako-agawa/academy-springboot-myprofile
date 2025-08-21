package com.spring.springbootapplication.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.UserRepository;

import com.spring.springbootapplication.service.UserService;
import com.spring.springbootapplication.web.ProfileForm; // ★ これを合わせる

import lombok.Value;

@Controller
public class ProfileController {
    private final UserRepository userRepository; // ユーザー情報を取得するためのリポジトリ
    private final Cloudinary cloudinary;
    private final UserService userService;
    @org.springframework.beans.factory.annotation.Value("${cloudinary.cloud-name}")
    private String cloudinaryCloudName;

    public ProfileController(UserService userService, Cloudinary cloudinary,
            UserRepository userRepository) {
        this.cloudinary = cloudinary;
        this.userRepository = userRepository;
        this.userService = userService;

    }

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        if (!model.containsAttribute("formModel")) {
            ProfileForm form = new ProfileForm();
            form.setId(user.getId());
            form.setBiography(user.getBiography());
            model.addAttribute("formModel", form);
        }

         model.addAttribute("cloudinaryCloudName", cloudinaryCloudName);
        model.addAttribute("user", user);
        return "profile";
    }

    // @PostMapping("/profile")
    // public String saveProfile(
    // @Validated @ModelAttribute("formModel") ProfileForm form,
    // org.springframework.validation.BindingResult binding,
    // Model model,
    // RedirectAttributes redirectAttrs) {

    // User loggedInUser = userService.getCurrentUser();

    // if (binding.hasErrors()) {
    // model.addAttribute("user", loggedInUser);
    // return "profile";
    // }
    // // 自己紹介文を更新
    // loggedInUser.setBiography(form.getBiography());

    // MultipartFile file = form.getThumbnail();
    // if (file != null && !file.isEmpty()) {
    // // ★ 3MB超ならフォームエラーにする
    // long max = 3L * 1024 * 1024;
    // if (file.getSize() > max) {
    // binding.rejectValue("thumbnail", "file.tooLarge", "画像は3MB以下にしてください");
    // model.addAttribute("user", loggedInUser);
    // return "profile";
    // }
    // try {
    // // String publicId = "myportfolio/user_" + loggedInUser.getId() +
    // "_thumbnail";
    // Map<?, ?> uploadResult = cloudinary.uploader().upload(
    // file.getBytes(),
    // ObjectUtils.asMap(
    // "use_filename", true, // 元ファイル名をベースに
    // "unique_filename", true, // ユニーク化（デフォルトtrueだが明示）
    // "folder", "myportfolio/users/" + loggedInUser.getId(),
    // "resource_type", "image"));

    // String newPublicId = (String) uploadResult.get("public_id"); // 例:
    // myportfolio/users/1/xxx_abc123
    // loggedInUser.setThumbnailPublicId(newPublicId);
    // } catch (IOException e) {
    // binding.rejectValue("thumbnail", "file.uploadFailed", "画像のアップロードに失敗しました");
    // model.addAttribute("user", loggedInUser);
    // return "profile";
    // }
    // }

    // userRepository.save(loggedInUser);
    // redirectAttrs.addFlashAttribute("message", "プロフィールを更新しました");
    // return "redirect:/";
    // }

    @PostMapping("/profile")
    public String saveProfile(@Validated @ModelAttribute("formModel") ProfileForm form,
            BindingResult binding,
            Model model,
            RedirectAttributes redirectAttrs) {

        User loggedInUser = userService.getCurrentUser();

        if (binding.hasErrors()) {
            model.addAttribute("user", loggedInUser);
            return "profile";
        }

        // 自己紹介文を更新
        loggedInUser.setBiography(form.getBiography());

        // Cloudinary の public_id を更新
        if (form.getThumbnailPublicId() != null && !form.getThumbnailPublicId().isBlank()) {
            loggedInUser.setThumbnailPublicId(form.getThumbnailPublicId());
        }

        userRepository.save(loggedInUser);
        redirectAttrs.addFlashAttribute("message", "プロフィールを更新しました");
        return "redirect:/";
    }
}