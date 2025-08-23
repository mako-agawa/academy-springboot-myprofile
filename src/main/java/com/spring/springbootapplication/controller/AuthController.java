package com.spring.springbootapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.service.CustomUserDetailsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;

    public AuthController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // ログインページ
    // ログインページ
    @GetMapping("/login")
    public String login(
            @org.springframework.web.bind.annotation.RequestParam(value = "error", required = false) String error,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (error != null) {
            // フラッシュメッセージとしてエラーをセット
            model.addAttribute("errorMessage", "メールアドレス、もしくはパスワードが間違っています");
        }
        return "auth/login";
    }

    // 登録ページ
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("formModel", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    @Transactional
    public String register(@ModelAttribute("formModel") @Validated User user,
            BindingResult bindingResult,
            HttpServletRequest request) throws ServletException {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        // 平文のパスワードを定義
        String rawPassword = user.getPasswordDigest();
        // ユーザー登録
        userDetailsService.registerUser(user);

        // セッションにも保存されるログイン処理
        request.login(user.getEmail(), rawPassword);

        return "redirect:/";
    }
}