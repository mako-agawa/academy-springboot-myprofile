package com.spring.springbootapplication.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class UserAuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // ★追加

    public UserAuthController(UserRepository userRepository,
            PasswordEncoder passwordEncoder) { // ★追加
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // ★追加
    }

    @RequestMapping("/login")
    @PreAuthorize("permitAll")
    public ModelAndView loginAuth(ModelAndView mav, @RequestParam(value = "error", required = false) String error) {
        mav.setViewName("login");
        System.out.println(error);
        if (error != null) {
            mav.addObject("msg", "メールアドレス、もしくはパスワードが間違っています");
        } else {
            mav.addObject("msg", "ユーザー名とパスワードを入力：");
        }
        System.out.println("==================");
        System.out.println("ログインしました: ");
        System.out.println("==================");
        return mav;
    }

    @GetMapping("/register")
    public ModelAndView registerInit(ModelAndView mav) {
        mav.addObject("formModel", new User());
        mav.setViewName("register");
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Transactional
    public ModelAndView form(@ModelAttribute("formModel") @Validated User user, BindingResult result,
            ModelAndView mav) {
        ModelAndView res = null;
        System.out.println(result.getFieldErrors());
        if (!result.hasErrors()) {
            String raw = user.getPasswordDigest();
            String hashed = passwordEncoder.encode(raw);
            user.setPasswordDigest(hashed);

            userRepository.save(user);
            System.out.println("==================");
            System.out.println("新規登録しました: ");
            System.out.println(user.getName());
            System.out.println(user.getEmail());
            System.out.println(user.getPasswordDigest());
            System.out.println("==================");
            res = new ModelAndView("redirect:/");
        } else {
            System.out.println("バリデーションエラーがあります。");
            mav.setViewName("register");
            mav.addObject("title", "新規登録");
            mav.addObject("message", "はじめての方はこちらから登録をお願いします。");
            mav.addObject("formModel", user); // エラーがある場合は入力内容を保持
            res = mav; // バリデーションエラーがある場合は再表示

        }

        return res;
    }

}
