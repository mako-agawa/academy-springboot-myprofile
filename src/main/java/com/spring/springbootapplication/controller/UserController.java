package com.spring.springbootapplication.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.UserRepository;

import jakarta.transaction.Transactional;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<User> userData = userRepository.findAll();
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "トップページです。");
        model.addAttribute("users", userData);
        System.out.println("==================");
        System.out.println("Top Page!!");
        System.out.println("==================");
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView register(ModelAndView mav) {
        mav.setViewName("register");
        mav.addObject("title", "新規登録");
        mav.addObject("message", "はじめての方はこちらから登録をお願いします。");
        mav.addObject("formModel", new User()); // ★ これが必要
        return mav;
    }

    @PostMapping("/register")
    @Transactional // 本当はService層に付けるのが定石
    public String form(@ModelAttribute("formModel") User user) {
        userRepository.save(user); // saveAndFlushは基本不要
        System.out.println("==================");
        System.out.println("新規登録しました: ");
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getPasswordDigest());
        System.out.println("==================");
        return "redirect:/";
    }

}
