package com.spring.springbootapplication.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.SkillChartService;
import com.spring.springbootapplication.service.UserService;

@Controller
public class IndexController {
    private final UserService userService;
    private final SkillChartService skillChartService;

    // コンストラクタ注入
    public IndexController(UserService userService, SkillChartService skillChartService) {
        this.userService = userService;
        this.skillChartService = skillChartService;
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

        // ← これを追加：MapをそのままModelへ
        // Map<String, Map<String, Integer>> chartData = skillChartService.getChartData(loggedInUser);
        // model.addAttribute("chartData", chartData);

        Map<String, Map<String, Integer>> skillchartData = skillChartService.getSkillChartData(loggedInUser.getId());
        model.addAttribute("chartData", skillchartData);
        System.out.println("==============");
        System.out.println(skillchartData);
        System.out.println("==============");
        return "index";
    }

}
