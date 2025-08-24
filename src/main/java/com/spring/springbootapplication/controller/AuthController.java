package com.spring.springbootapplication.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.spring.springbootapplication.repository.UserRepository;
import com.spring.springbootapplication.service.CustomUserDetailsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private UserRepository userRepository;

    public AuthController(CustomUserDetailsService userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    // ログインページ
    // ログインページ
    @GetMapping("/login")
    public String login(
            @org.springframework.web.bind.annotation.RequestParam(value = "error", required = false) String error,
            RedirectAttributes redirectAttributes,
            Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/";
        }
        if (error != null) {
            // フラッシュメッセージとしてエラーをセット
            model.addAttribute("errorMessage", "メールアドレス、もしくはパスワードが間違っています");
        }
        return "auth/login";
    }

    // 登録ページ
    @GetMapping("/register")
    public String registerForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/";
        }
        model.addAttribute("formModel", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    @Transactional
    public String register(@ModelAttribute("formModel") @Validated User user,
            BindingResult bindingResult,
            HttpServletRequest request) throws ServletException {
        // 他のバリデーションに引っかかったらそのまま返す
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        // 事前にメール重複チェック（大小無視したいなら existsByEmailIgnoreCase を使用）
        // 余計な空白を消しておくと実運用での取りこぼしが減ります
        String email = user.getEmail().trim();
        user.setEmail(email);
        if (userRepository.existsByEmailIgnoreCase(email)) {
            bindingResult.rejectValue("email", "duplicate", "このメールアドレスは既に登録されています");
            return "auth/register";
        }

        // 平文パスワードを退避（後で request.login に使う）
        String rawPassword = user.getPasswordDigest();

        try {
            // 登録（内部でハッシュ化）
            userDetailsService.registerUser(user);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // 同時登録等でDBの一意制約に引っかかった場合の保険
            bindingResult.rejectValue("email", "duplicate", "このメールアドレスは既に登録されています");
            return "auth/register";
        }

        // 登録直後にログイン
        request.login(user.getEmail(), rawPassword);
        return "redirect:/";
    }
}