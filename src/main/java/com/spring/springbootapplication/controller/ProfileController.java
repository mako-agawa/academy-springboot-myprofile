package com.spring.springbootapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.qos.logback.core.model.Model;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profile(Model model) {

        return "profile"; // 先頭スラッシュなし
    }

}
