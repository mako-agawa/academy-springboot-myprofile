package com.spring.springbootapplication.controller;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.UserRepository;
import com.spring.springbootapplication.service.UserService;

@Controller
public class IndexController {

    private final UserRepository userRepository;
    private final UserService userService;

    // コンストラクタ注入
    public IndexController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserDetails user) {
        User loggedInUser = userService.getCurrentUser();

        model.addAttribute("title", "Welcome");
        model.addAttribute("currentUserName", loggedInUser.getName());
        model.addAttribute("currentUserBio", loggedInUser.getBiography());
        System.out.println("==================");
        System.out.println("loggedInUser:" + loggedInUser.getName());
        System.out.println("currentUserBio:" + loggedInUser.getBiography());
        System.out.println("==================");
        return "index";
    }
        

}
