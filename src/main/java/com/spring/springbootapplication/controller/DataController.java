// src/main/java/com/spring/springbootapplication/controller/DataController.java
package com.spring.springbootapplication.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.CategoryRepository;
import com.spring.springbootapplication.repository.UserRepository;

@Controller
public class DataController {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public DataController(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    
    // 画面表示：カテゴリとユーザーを同時に渡す
    @GetMapping("/data")
    public String getData(Model model) {
        List<Category> categoryData = categoryRepository.findAll();
        List<User> userData = userRepository.findAll();

        model.addAttribute("categories", categoryData);
        model.addAttribute("users", userData);

        System.out.println("=================");
        System.out.println("categories = " + categoryData);
        System.out.println("users      = " + userData);
        System.out.println("=================");

        return "data"; // src/main/resources/templates/data.html
    }

    // API: カテゴリ一覧（JSON）
    @GetMapping("/data/api/categories")
    @ResponseBody
    public List<Category> apiCategories() {
        return categoryRepository.findAll();
    }

    // API: ユーザー一覧（JSON）
    @GetMapping("/data/api/users")
    @ResponseBody
    public List<User> apiUsers() {
        return userRepository.findAll();
    }
}