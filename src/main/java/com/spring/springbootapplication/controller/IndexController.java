package com.spring.springbootapplication.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.UserService;

@Controller
public class IndexController {
    private final UserService userService;

    // コンストラクタ注入
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserDetails user) {
        User loggedInUser = userService.getCurrentUser();

        model.addAttribute("title", "Welcome");
        model.addAttribute("currentUserName", loggedInUser.getName());
        model.addAttribute("currentUserBio", loggedInUser.getBiography());
        if (loggedInUser.getThumbnailPublicId() != null) {
            model.addAttribute("thumbnailUrl",
                    "https://res.cloudinary.com/djklqnmen/image/upload/"
                            + loggedInUser.getThumbnailPublicId() + ".jpg");
        } else {
            model.addAttribute("thumbnailUrl",
                    "/image/default-avatar.avif");
        }

        System.out.println("==================");
        System.out.println("loggedInUser:" + loggedInUser.getName());
        System.out.println("currentUserBio:" + loggedInUser.getBiography());
        System.out.println("thumbnailUrl:" + model.getAttribute("thumbnailUrl"));
        System.out.println("==================");
        return "index";
    }
}
