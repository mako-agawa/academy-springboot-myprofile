package com.spring.springbootapplication.controller;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.UserRepository;

@Controller
public class IndexController {

    private final UserRepository userRepository;

    // コンストラクタ注入
    public IndexController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<User> userData = userRepository.findAll();
        model.addAttribute("title", "Welcome");
        model.addAttribute("users", userData);
        System.out.println("==================");
        System.out.println("Top Page!!");
        System.out.println("==================");
        return "index";
    }

}
