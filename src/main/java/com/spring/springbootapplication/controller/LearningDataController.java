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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.CategoryRepository;
import com.spring.springbootapplication.service.LearningDataService;
import com.spring.springbootapplication.service.UserService;
import com.spring.springbootapplication.web.SkillForm;
import com.spring.springbootapplication.web.TimeRecordFrom;

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
                model.addAttribute("targetMonthValue", targetMonth.toString()); // ← ★追加

                model.addAttribute("currentMonthValue", YearMonth.now().toString());
                model.addAttribute("currentMonthLabel", YearMonth.now().getMonthValue() + "月");
                model.addAttribute("lastMonthValue", YearMonth.now().minusMonths(1).toString());
                model.addAttribute("lastMonthLabel", YearMonth.now().minusMonths(1).getMonthValue() + "月");
                model.addAttribute("twoMonthsAgoValue", YearMonth.now().minusMonths(2).toString());
                model.addAttribute("twoMonthsAgoLabel", YearMonth.now().minusMonths(2).getMonthValue() + "月");

                User currentUser = userService.getCurrentUser();
                TimeRecordFrom timeRecordForm = new TimeRecordFrom();

                model.addAttribute("timeRecordForm", timeRecordForm);

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
        public String createSkill(
                        @ModelAttribute("skillformModel") @Validated SkillForm form,
                        BindingResult result,
                        @RequestParam("month") String monthParam,
                        Model model) {

                User currentUser = userService.getCurrentUser();

                // learningDateの補完（フォーム未設定ならmonthParamから）
                if (form.getLearningDate() == null) {
                        form.setLearningDate(YearMonth.parse(monthParam).atDay(1).atStartOfDay());
                }

                // ここでカテゴリタイトルを解決（以降で何度も使う）
                String categoryTitle = categoryRepository.findById(form.getCategoryId())
                                .map(Category::getTitle)
                                .orElse("未分類");

                if (result.hasErrors()) {
                        // ★ エラー再表示に必要な属性を必ず詰める
                        model.addAttribute("selectedCategory", categoryTitle);
                        model.addAttribute("targetMonthValue", monthParam);
                        return "skill/new";
                }

                LearningData data = new LearningData();
                data.setTitle(form.getTitle());
                data.setTimeRecord(form.getTimeRecord());
                data.setLearningDate(form.getLearningDate());
                data.setUser(currentUser);

                Category category = categoryRepository.findById(form.getCategoryId())
                                .orElseThrow(() -> new RuntimeException("カテゴリが見つかりません"));
                data.setCategory(category);

                try {
                        learningDataService.saveWithValidation(data);
                } catch (IllegalArgumentException e) {
                        // ★ 例外（重複など）時も同様に再表示へ戻す
                        result.rejectValue("title", "duplicate", e.getMessage());
                        model.addAttribute("selectedCategory", categoryTitle);
                        model.addAttribute("targetMonthValue", monthParam);
                        return "skill/new";
                }

                // 正常時のみ一覧へ
                return "redirect:/skill?month=" + monthParam;
        }

        @PostMapping("/skill/update")
        public String updateTimeRecord(
                        @RequestParam("id") Long id,
                        @RequestParam("month") String monthParam,
                        @ModelAttribute("timeRecordForm") @Validated TimeRecordFrom timeRecordFromorm,
                        BindingResult result,
                        RedirectAttributes redirectAttrs) {

                if (result.hasErrors()) {
                        // 簡易的に一覧へ戻す。必要ならエラーフラッシュを載せる
                        redirectAttrs.addFlashAttribute("updateError", "入力値に誤りがあります。");
                        return "redirect:/skill?month=" + monthParam;
                }

                User loggedInUser = userService.getCurrentUser();

                LearningData learningData = learningDataService.findById(id)
                                .orElseThrow(() -> new RuntimeException("データが見つかりません"));

                if (!learningData.getUser().getId().equals(loggedInUser.getId())) {
                        throw new RuntimeException("権限がありません");
                }

                learningData.setTimeRecord(timeRecordFromorm.getTimeRecord());
                learningDataService.save(learningData);

                redirectAttrs.addFlashAttribute("updateSuccess", true);
                redirectAttrs.addFlashAttribute("updatedTitle", learningData.getTitle());
                redirectAttrs.addFlashAttribute("updatedTime", learningData.getTimeRecord());
                return "redirect:/skill?month=" + monthParam;
        }

        @PostMapping("/skill/delete")
        public String deleteSkill(@RequestParam("id") Long id,
                        @RequestParam("month") String monthParam,
                        RedirectAttributes redirectAttrs) {
                User loggedInUser = userService.getCurrentUser();

                LearningData learningData = learningDataService.findById(id)
                                .orElseThrow(() -> new RuntimeException("データが見つかりません"));

                if (!learningData.getUser().getId().equals(loggedInUser.getId())) {
                        throw new RuntimeException("権限がありません");
                }
                // ★ 削除前にタイトルを保持してフラッシュへ
                String title = learningData.getTitle();

                learningDataService.deleteById(id);

                redirectAttrs.addFlashAttribute("deleteSuccess", true);
                redirectAttrs.addFlashAttribute("deletedTitle", title);
                return "redirect:/skill?month=" + monthParam;
        }

}
