package com.spring.springbootapplication.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @RequestMapping("/")
    // @PreAuthorize("permitAll")
    public ModelAndView index(ModelAndView mav) {
        mav.setViewName("index");
        mav.addObject("title", "Welcome");
        mav.addObject("message", "Welcome to the Spring Boot Application!");
        return mav;
    }
    
}
