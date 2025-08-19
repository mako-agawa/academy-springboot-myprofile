// src/main/java/com/spring/springbootapplication/controller/DataController.java
package com.spring.springbootapplication.controller;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.CategoryRepository;
import com.spring.springbootapplication.repository.LearningDataRepository;
import com.spring.springbootapplication.repository.UserRepository;
import com.spring.springbootapplication.service.LearningDataService;
import com.spring.springbootapplication.service.UserService;

@Controller
public class SkillController {
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final LearningDataService learningDataService;

    public SkillController(UserService userService, CategoryRepository categoryRepository, LearningDataService learningDataService,
            UserRepository userRepository,
            LearningDataRepository learningDataRepository) {
        this.categoryRepository = categoryRepository;
        this.learningDataService = learningDataService;
        this.userService = userService;

    }


    // 画面表示：カテゴリとユーザーを同時に渡す
    @GetMapping("/skill")
    public String skill(
            @RequestParam(value = "month", required = false) String monthParam,
            Model model,
            @AuthenticationPrincipal UserDetails user) {

        YearMonth targetMonth = (monthParam == null) ? YearMonth.now() : YearMonth.parse(monthParam);

        int targetMonthLabel = targetMonth.getMonthValue();
        System.out.println("==========================");
        System.out.println("targetMonthLabel: " + targetMonthLabel);
        System.out.println("==========================");

        // セレクトボックス用に "2025-08" と "8月" を両方渡す
        model.addAttribute("currentMonthValue", YearMonth.now().toString()); // 2025-08
        model.addAttribute("currentMonthLabel", YearMonth.now().getMonthValue() + "月");
        model.addAttribute("lastMonthValue", YearMonth.now().minusMonths(1).toString()); // 2025-07
        model.addAttribute("lastMonthLabel", YearMonth.now().minusMonths(1).getMonthValue() + "月");
        model.addAttribute("twoMonthsAgoValue", YearMonth.now().minusMonths(2).toString()); // 2025-06
        model.addAttribute("twoMonthsAgoLabel", YearMonth.now().minusMonths(2).getMonthValue() + "月");
        model.addAttribute("targetMonthLabel", targetMonthLabel  + "月");

        User currentUser = userService.getCurrentUser();

        // バックエンド
        List<Category> backEndCategory = categoryRepository.findByTitle("バックエンド");
        if (!backEndCategory.isEmpty()) {
            Long backendCategoryId = backEndCategory.get(0).getId();
            List<LearningData> backEndLearningDatas = learningDataService
                    .getLearningDataByCategoryAndMonth(currentUser.getId(), backendCategoryId, targetMonth);
            model.addAttribute("backEndLearningDatas", backEndLearningDatas);
        }

        // フロントエンド
        List<Category> frontEndCategory = categoryRepository.findByTitle("フロントエンド");
        if (!frontEndCategory.isEmpty()) {
            Long frontEndCategoryId = frontEndCategory.get(0).getId();
            List<LearningData> frontEndLearningDatas = learningDataService
                    .getLearningDataByCategoryAndMonth(currentUser.getId(), frontEndCategoryId, targetMonth);
            model.addAttribute("frontEndLearningDatas", frontEndLearningDatas);
        }

        // インフラ
        List<Category> infraCategory = categoryRepository.findByTitle("インフラ");
        if (!infraCategory.isEmpty()) {
            Long infraCategoryId = infraCategory.get(0).getId();
            List<LearningData> infraLearningDatas = learningDataService
                    .getLearningDataByCategoryAndMonth(currentUser.getId(), infraCategoryId, targetMonth);
            model.addAttribute("infraLearningDatas", infraLearningDatas);
        }

        return "skill/index";
    }
}
