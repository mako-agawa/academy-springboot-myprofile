package com.spring.springbootapplication.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    // @PostMapping("/register")
    // @Transactional // 本当はService層に付けるのが定石
    // public String form(@ModelAttribute("formModel") @Validated User user) {
    // if( {
    // return "register"; // バリデーションエラーがある場合は再表示
    // }else{
    // userRepository.save(user); // saveAndFlushは基本不要
    // System.out.println("==================");
    // System.out.println("新規登録しました: ");
    // System.out.println(user.getName());
    // System.out.println(user.getEmail());
    // System.out.println(user.getPasswordDigest());
    // System.out.println("==================");
    // return "redirect:/";
    // }

    // }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Transactional
    public ModelAndView form(@ModelAttribute("formModel") @Validated User user, BindingResult result,
            ModelAndView mav) {
        ModelAndView res = null;
        System.out.println(result.getFieldErrors());
        if (!result.hasErrors()) {
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
