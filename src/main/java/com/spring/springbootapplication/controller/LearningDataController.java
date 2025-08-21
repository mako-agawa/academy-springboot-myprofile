// src/main/java/com/spring/springbootapplication/controller/DataController.java
package com.spring.springbootapplication.controller;

import java.time.YearMonth;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.CategoryRepository;
import com.spring.springbootapplication.service.LearningDataService;
import com.spring.springbootapplication.service.UserService;
import com.spring.springbootapplication.web.SkillForm;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
public class LearningDataController {
        private final LearningDataService learningDataService;
        private final UserService userService;
        private final CategoryRepository categoryRepository;

        public LearningDataController(LearningDataService learningDataService,
                        UserService userService,
                        CategoryRepository categoryRepository) {
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
                model.addAttribute("targetMonth", targetMonth);

                model.addAttribute("currentMonthValue", YearMonth.now().toString());
                model.addAttribute("currentMonthLabel", YearMonth.now().getMonthValue() + "月");
                model.addAttribute("lastMonthValue", YearMonth.now().minusMonths(1).toString());
                model.addAttribute("lastMonthLabel", YearMonth.now().minusMonths(1).getMonthValue() + "月");
                model.addAttribute("twoMonthsAgoValue", YearMonth.now().minusMonths(2).toString());
                model.addAttribute("twoMonthsAgoLabel", YearMonth.now().minusMonths(2).getMonthValue() + "月");

                User currentUser = userService.getCurrentUser();

                model.addAttribute("backEndLearningDatas",
                                learningDataService.getLearningDataByCategoryNameAndMonth(currentUser.getId(), "バックエンド",
                                                targetMonth));

                model.addAttribute("frontEndLearningDatas",
                                learningDataService.getLearningDataByCategoryNameAndMonth(currentUser.getId(),
                                                "フロントエンド", targetMonth));

                model.addAttribute("infraLearningDatas",
                                learningDataService.getLearningDataByCategoryNameAndMonth(currentUser.getId(), "インフラ",
                                                targetMonth));

                return "skill/index";
        }

        // 新規スキル登録画面表示
        @GetMapping("/skill/new")
        public String newSkill(@RequestParam(value = "category") String categoryTitle,
                        @RequestParam(value = "month", required = false) String monthParam,
                        Model model) {

                String targetMonthValue = (monthParam == null || monthParam.isBlank())
                                ? YearMonth.now().toString()
                                : monthParam;

                // フォームの初期化
                User currentUser = userService.getCurrentUser();
                SkillForm form = new SkillForm();
                form.setUserId(currentUser.getId());
                form.setLearningDate(YearMonth.parse(targetMonthValue).atDay(1).atStartOfDay());

                Long categoryId = categoryRepository.findByTitle(categoryTitle)
                                .stream().findFirst()
                                .map(Category::getId)
                                .orElseThrow(() -> new RuntimeException("カテゴリが見つかりません"));
                form.setCategoryId(categoryId);

                model.addAttribute("skillformModel", form);
                model.addAttribute("selectedCategory", categoryTitle);
                model.addAttribute("targetMonthValue", targetMonthValue);

                return "skill/new";
        }

        @PostMapping("/skill/new")
        @Transactional
        public String createSkill(
                        @ModelAttribute("skillformModel") @Valid SkillForm form,
                        @RequestParam("month") String monthParam,
                        BindingResult result,
                        Model model) {

                if (result.hasErrors()) {
                        // 必要なmodelを再セット
                        model.addAttribute("targetMonthValue", monthParam);
                        model.addAttribute("selectedCategoryId", form.getCategoryId());
                        model.addAttribute("selectedCategory", "カテゴリ名");
                        return "skill/new";
                }

                User currentUser = userService.getCurrentUser();

                LearningData data = new LearningData();
                data.setTitle(form.getTitle());
                data.setTimeRecord(form.getTimeRecord());
                data.setUser(currentUser);

                Category category = categoryRepository.findById(form.getCategoryId())
                                .orElseThrow(() -> new RuntimeException("カテゴリが見つかりません"));
                data.setCategory(category);

                // ★ monthParam (例: "2025-08") を LocalDateTime に変換してセット
                YearMonth ym = YearMonth.parse(monthParam);
                data.setLearningDate(ym.atDay(1).atStartOfDay());

                try {
                        learningDataService.saveWithValidation(data);
                } catch (IllegalArgumentException e) {
                        result.rejectValue("title", "duplicate", e.getMessage());
                        model.addAttribute("targetMonthValue", monthParam);
                        model.addAttribute("selectedCategoryId", category.getId());
                        model.addAttribute("selectedCategory", category.getTitle());
                        return "skill/new";
                }

                return "redirect:/skill?month=" + monthParam;
        }

        @PostMapping("/skill/delete")

        public String deleteSkill(@RequestParam("id") Long id,
                        @RequestParam("month") String monthParam) {
                User loggedInUser = userService.getCurrentUser();

                LearningData learningData = learningDataService.findById(id)
                                .orElseThrow(() -> new RuntimeException("データが見つかりません"));

                // ログインユーザーのデータかチェックしてセキュリティ確保
                if (!learningData.getUser().getId().equals(loggedInUser.getId())) {
                        throw new RuntimeException("権限がありません");
                }

                learningDataService.deleteById(id);

                // 元の月にリダイレクト
                return "redirect:/skill?month=" + monthParam;
        }

}
