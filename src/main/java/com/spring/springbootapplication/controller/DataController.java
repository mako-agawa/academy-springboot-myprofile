// src/main/java/com/spring/springbootapplication/controller/DataController.java
package com.spring.springbootapplication.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.repository.CategoryRepository;

@Controller
public class DataController {

    private final CategoryRepository categoryRepository;

    public DataController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // /data にアクセスしたら data.html を返す
    @GetMapping("/data")
    public String index(Model model) {
        List<Category> categoryData = categoryRepository.findAll(); // [frontEnd, backEnd, infra]
        model.addAttribute("categories", categoryData); // ← テンプレに渡す名前を "categories" に統一
        System.out.println("=================");
        System.out.println("categories = " + categoryData);
        System.out.println("=================");
        return "data"; // src/main/resources/templates/data.html
    }

    @GetMapping("/data/api")
    @ResponseBody
    public List<Category> api() {
        return categoryRepository.findAll();
    }

}