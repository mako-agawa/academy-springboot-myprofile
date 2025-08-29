package com.spring.springbootapplication.service;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.LearningDataRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkillChartService {
    private final LearningDataService learningDataService;
    private final LearningDataRepository learningDataRepository;

    public SkillChartService(LearningDataRepository learningDataRepository, LearningDataService learningDataService) {
        this.learningDataService = learningDataService;
        this.learningDataRepository = learningDataRepository;

    }

    // ====パターンA===
    public Map<String, Map<String, Integer>> getChartData(User loggedInUser) {
        User user = loggedInUser;

        YearMonth currentMonth = YearMonth.now();
        YearMonth lastMonth = currentMonth.minusMonths(1);
        YearMonth twoMonthsAgo = currentMonth.minusMonths(2);

        Map<String, Map<String, Integer>> result = new HashMap<>();

        result.put("backend", buildCategoryData(user.getId(), "バックエンド", currentMonth, lastMonth, twoMonthsAgo));
        result.put("frontend", buildCategoryData(user.getId(), "フロントエンド", currentMonth, lastMonth, twoMonthsAgo));
        result.put("infra", buildCategoryData(user.getId(), "インフラ", currentMonth, lastMonth, twoMonthsAgo));

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
    // ====パターンB===

    public Map<String, Map<String, Integer>> getSkillChartData(Long userId) {
        YearMonth current = YearMonth.now();
        LocalDateTime startTwo = current.minusMonths(2).atDay(1).atStartOfDay(); // 先々月1日 00:00
        LocalDateTime startNext = current.plusMonths(1).atDay(1).atStartOfDay(); // 来月1日 00:00

        // BETWEEN は両端含むので end を 1ns 手前に寄せるか、@Query(半開区間)にする
        LocalDateTime endInclusive = startNext.minusNanos(1);

        List<LearningData> dataList = learningDataRepository.findByUserIdAndLearningDateBetween(userId, startTwo,
                endInclusive);

        Map<String, Map<String, Integer>> skillChartData = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (LearningData data : dataList) {
            String month = data.getLearningDate().format(formatter); // "YYYY-MM"
            String category = data.getCategory().getTitle();

            skillChartData.putIfAbsent(month, new HashMap<>());
            skillChartData.get(month).put(category,
                    skillChartData.get(month).getOrDefault(category, 0) + data.getTimeRecord());
        }

        // カテゴリの学習時間がない場合のデフォルト処理
        for (String month : List.of(startTwo.format(formatter), current.minusMonths(1).format(formatter),
                current.format(formatter))) {
            skillChartData.putIfAbsent(month, new HashMap<>());
            skillChartData.get(month).putIfAbsent("バックエンド", 0);
            skillChartData.get(month).putIfAbsent("フロントエンド", 0);
            skillChartData.get(month).putIfAbsent("インフラ", 0);
        }

        return skillChartData;
    }
}
