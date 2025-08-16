// src/main/java/com/spring/springbootapplication/controller/DataController.java
package com.spring.springbootapplication.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.CategoryRepository;
import com.spring.springbootapplication.repository.UserRepository;

@Controller
public class SkillController {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public SkillController(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // 画面表示：カテゴリとユーザーを同時に渡す
    @GetMapping("/skill/")
    public String skill() {
        return "skill/index"; // 対応するテンプレートを返す
    }

    // API: カテゴリ一覧（JSON）
    @GetMapping("/skill/api/categories")
    @ResponseBody
    public List<Category> apiCategories() {
        return categoryRepository.findAll();
    }

    // API: ユーザー一覧（JSON）
    @GetMapping("/skill/api/users")
    @ResponseBody
    public List<User> apiUsers() {
        return userRepository.findAll();
    }
}