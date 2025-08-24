package com.spring.springbootapplication.service;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkillChartService {
    private final LearningDataService learningDataService;
    private final UserService userService;

    public SkillChartService(LearningDataService learningDataService, UserService userService) {
        this.learningDataService = learningDataService;
        this.userService = userService;
    }

    public Map<String, Map<String, Integer>> getChartData() {
        User user = userService.getCurrentUser();

        YearMonth currentMonth = YearMonth.now();
        YearMonth lastMonth = currentMonth.minusMonths(1);
        YearMonth twoMonthsAgo = currentMonth.minusMonths(2);

        Map<String, Map<String, Integer>> result = new HashMap<>();

        result.put("backend", buildCategoryData(user.getId(), "バックエンド", currentMonth, lastMonth, twoMonthsAgo));
        result.put("frontend", buildCategoryData(user.getId(), "フロントエンド", currentMonth, lastMonth, twoMonthsAgo));
        result.put("infra", buildCategoryData(user.getId(), "インフラ", currentMonth, lastMonth, twoMonthsAgo));

        System.out.println("================");
        System.out.println(buildCategoryData(user.getId(), "バックエンド", currentMonth, lastMonth, twoMonthsAgo));
        System.out.println(buildCategoryData(user.getId(), "フロントエンド", currentMonth, lastMonth, twoMonthsAgo));
        System.out.println(buildCategoryData(user.getId(), "インフラ", currentMonth, lastMonth, twoMonthsAgo));
        System.out.println("================");

        return result;
    }

    private Map<String, Integer> buildCategoryData(Long userId, String category, YearMonth current, YearMonth last,
            YearMonth twoAgo) {
        Map<String, Integer> data = new HashMap<>();
        data.put("currentMonth", sumTime(userId, category, current));
        data.put("lastMonth", sumTime(userId, category, last));
        data.put("twoMonthsAgo", sumTime(userId, category, twoAgo));
        return data;
    }

    private int sumTime(Long userId, String category, YearMonth month) {
        List<LearningData> datas = learningDataService.getLearningDataByCategoryNameAndMonth(userId, category, month);

        return datas.stream().mapToInt(LearningData::getTimeRecord).sum();

    }
}