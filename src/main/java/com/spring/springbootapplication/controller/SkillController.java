// src/main/java/com/spring/springbootapplication/controller/DataController.java
package com.spring.springbootapplication.controller;

import java.time.YearMonth;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.CategoryRepository;
import com.spring.springbootapplication.repository.LearningDataRepository;

import com.spring.springbootapplication.service.LearningDataService;
import com.spring.springbootapplication.service.UserService;

import com.spring.springbootapplication.web.SkillForm;

@Controller
public class SkillController {
    private final UserService userService;
    private final LearningDataService learningDataService;
    private final CategoryRepository categoryRepository;

    public SkillController(UserService userService,
            CategoryRepository categoryRepository,
            LearningDataService learningDataService,
            LearningDataRepository learningDataRepository) {

        this.learningDataService = learningDataService;
        this.userService = userService;
        this.categoryRepository = categoryRepository;

    }

    // 画面表示：カテゴリとユーザーを同時に渡す
    @GetMapping("/skill")
    public String skill(
            @RequestParam(value = "month", required = false) String monthParam,
            Model model,
            @AuthenticationPrincipal UserDetails user) {

        YearMonth targetMonth = (monthParam == null) ? YearMonth.now() : YearMonth.parse(monthParam);
        model.addAttribute("targetMonthLabel", targetMonth.getMonthValue() + "月");

        model.addAttribute("currentMonthValue", YearMonth.now().toString());
        model.addAttribute("currentMonthLabel", YearMonth.now().getMonthValue() + "月");
        model.addAttribute("lastMonthValue", YearMonth.now().minusMonths(1).toString());
        model.addAttribute("lastMonthLabel", YearMonth.now().minusMonths(1).getMonthValue() + "月");
        model.addAttribute("twoMonthsAgoValue", YearMonth.now().minusMonths(2).toString());
        model.addAttribute("twoMonthsAgoLabel", YearMonth.now().minusMonths(2).getMonthValue() + "月");

        User currentUser = userService.getCurrentUser();

        model.addAttribute("backEndLearningDatas",
                learningDataService.getLearningDataByCategoryNameAndMonth(currentUser.getId(), "バックエンド", targetMonth));

        model.addAttribute("frontEndLearningDatas",
                learningDataService.getLearningDataByCategoryNameAndMonth(currentUser.getId(), "フロントエンド", targetMonth));

        model.addAttribute("infraLearningDatas",
                learningDataService.getLearningDataByCategoryNameAndMonth(currentUser.getId(), "インフラ", targetMonth));

        return "skill/index";
    }

    @GetMapping("/skill/new")
    public String newSkill(@RequestParam(value = "category", required = true) String categoryTitle,
            @RequestParam(value = "month", required = true) String monthParam,
            Model model) {

        User currentUser = userService.getCurrentUser();
        SkillForm form = new SkillForm();
        form.setUserId(currentUser.getId());
        form.setLearningDate(YearMonth.parse(monthParam).atDay(1).atStartOfDay());
        YearMonth targetMonth;
        if (monthParam == null || monthParam.isBlank()) {
            targetMonth = YearMonth.now();
        } else {
            targetMonth = YearMonth.parse(monthParam); // yyyy-MM 形式が必要
        }

        // categoryId を hidden で渡せるようにする
        Long categoryId = categoryRepository.findByTitle(categoryTitle)
                .stream().findFirst()
                .map(Category::getId)
                .orElseThrow(() -> new RuntimeException("カテゴリが見つかりません"));
        form.setCategoryId(categoryId);

        model.addAttribute("skillformModel", form);
        model.addAttribute("selectedCategory", categoryTitle);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("targetMonthValue", monthParam);
        model.addAttribute("targetMonthValue", targetMonth.toString()); // ← yyyy-MM

        System.out.println("Selected Month: " + monthParam);
        System.out.println("Selected Category: " + categoryTitle);
        System.out.println("CurrentUser " + currentUser.getName());
        return "skill/new";
    }

    @PostMapping("/skill/new")
    public String createSkill(
            @ModelAttribute("skillformModel") SkillForm form,
            @RequestParam("month") String monthParam) {
        User currentUser = userService.getCurrentUser();
        // LearningData に変換
        LearningData data = new LearningData();
        data.setTitle(form.getTitle());
        data.setTimeRecord(form.getTimeRecord());
        data.setLearningDate(form.getLearningDate());
        data.setUser(currentUser);

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new RuntimeException("カテゴリが見つかりません"));
        data.setCategory(category);

        learningDataService.save(data);

        // 保存後に元の month 画面へリダイレクト
        return "redirect:/skill?month=" + monthParam;
    }
}
