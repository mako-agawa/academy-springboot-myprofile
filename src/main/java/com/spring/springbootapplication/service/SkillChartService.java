package com.spring.springbootapplication.service;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.CategoryRepository;
import com.spring.springbootapplication.repository.LearningDataAggRepository;

import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// @Service
// public class SkillChartService {
//     private final LearningDataService learningDataService;
//     private final UserService userService;

//     public SkillChartService(LearningDataService learningDataService, UserService userService) {
//         this.learningDataService = learningDataService;
//         this.userService = userService;
//     }

//     public Map<String, Map<String, Integer>> getChartData() {
//         User user = userService.getCurrentUser();

//         YearMonth currentMonth = YearMonth.now();
//         YearMonth lastMonth = currentMonth.minusMonths(1);
//         YearMonth twoMonthsAgo = currentMonth.minusMonths(2);

//         Map<String, Map<String, Integer>> result = new HashMap<>();

//         result.put("backend", buildCategoryData(user.getId(), "バックエンド", currentMonth, lastMonth, twoMonthsAgo));
//         result.put("frontend", buildCategoryData(user.getId(), "フロントエンド", currentMonth, lastMonth, twoMonthsAgo));
//         result.put("infra", buildCategoryData(user.getId(), "インフラ", currentMonth, lastMonth, twoMonthsAgo));

//         return result;
//     }

//     private Map<String, Integer> buildCategoryData(Long userId, String category, YearMonth current, YearMonth last,
//             YearMonth twoAgo) {
//         Map<String, Integer> data = new HashMap<>();
//         data.put("currentMonth", sumTime(userId, category, current));
//         data.put("lastMonth", sumTime(userId, category, last));
//         data.put("twoMonthsAgo", sumTime(userId, category, twoAgo));
//         return data;
//     }

//     private int sumTime(Long userId, String category, YearMonth month) {
//         List<LearningData> datas = learningDataService.getLearningDataByCategoryNameAndMonth(userId, category, month);

//         return datas.stream().mapToInt(LearningData::getTimeRecord).sum();

//     }
// }

@Service
public class SkillChartService {
    private final LearningDataAggRepository aggRepo;
    private final CategoryRepository categoryRepo;
    private final UserService userService;

    public SkillChartService(LearningDataAggRepository aggRepo, CategoryRepository categoryRepo, UserService userService){
        this.aggRepo = aggRepo;
        this.categoryRepo = categoryRepo;
        this.userService = userService;

    }

    public Map<String, Map<String,Integer>> getChartData() {
        var user = userService.getCurrentUser();

        var current = YearMonth.now();
        var startCurr = current.atDay(1).atStartOfDay();
        var startLast = current.minusMonths(1).atDay(1).atStartOfDay();
        var startTwo  = current.minusMonths(2).atDay(1).atStartOfDay();
        var startNext = current.plusMonths(1).atDay(1).atStartOfDay(); // ← 半開区間

        Map<String, Map<String,Integer>> result = new HashMap<>();

        // 事前にカテゴリIDを引いておく（1回だけ）
        Map<String, Long> categoryIdByTitle = categoryRepo.findAll().stream()
            .filter(c -> List.of("バックエンド","フロントエンド","インフラ").contains(c.getTitle()))
            .collect(Collectors.toMap(c -> c.getTitle(), c -> c.getId()));

        for (var title : List.of("バックエンド","フロントエンド","インフラ")) {
            Long cid = categoryIdByTitle.get(title);
            var row = aggRepo.sumForCategory3Months(
                user.getId(), cid, startTwo, startLast, startCurr, startNext);

            Map<String,Integer> map = new HashMap<>();
            map.put("twoMonthsAgo",  nullToZero(row.getTwoMonthsAgo()));
            map.put("lastMonth",     nullToZero(row.getLastMonth()));
            map.put("currentMonth",  nullToZero(row.getCurrentMonth()));

            result.put(switch (title) {
                case "バックエンド" -> "backend";
                case "フロントエンド" -> "frontend";
                default -> "infra";
            }, map);
        }
        return result;
    }

    private static int nullToZero(Integer v) { return v == null ? 0 : v; }
}