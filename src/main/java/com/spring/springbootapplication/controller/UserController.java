package com.spring.springbootapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @RequestMapping("/")
    // @PreAuthorize("permitAll")
    public ModelAndView index(ModelAndView mav) {
        mav.setViewName("index");
        mav.addObject("title", "Welcome");
        mav.addObject("message", "Welcome to the Spring Boot Application!");
        return mav;
    }
    
        @RequestMapping("/register")
    // @PreAuthorize("permitAll")
    public ModelAndView register(ModelAndView mav) {
        mav.setViewName("register");
        mav.addObject("title", "新規登録");
        mav.addObject("message", "はじめての方はこちらから登録をお願いします。");
        return mav;
    }
}
