package com.spring.springbootapplication.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.repository.CategoryRepository;
import com.spring.springbootapplication.repository.LearningDataRepository;

@Service
public class LearningDataService {
    private final LearningDataRepository learningDataRepository;
    private final CategoryRepository categoryRepository;

    public LearningDataService(LearningDataRepository learningDataRepository,
            CategoryRepository categoryRepository) {
        this.learningDataRepository = learningDataRepository;
        this.categoryRepository = categoryRepository;
    }

    // 保存用メソッド
    public LearningData save(LearningData data) {
        return learningDataRepository.save(data);
    }

    // 特定のユーザーとカテゴリに基づいて、指定された月の学習データを取得するメソッド
    public List<LearningData> getLearningDataByCategoryAndMonth(Long userId, Long categoryId, YearMonth month) {
        LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
        return learningDataRepository.findByUserIdAndCategoryIdAndLearningDateBetween(
                userId, categoryId, startOfMonth, endOfMonth);
    }

    public List<LearningData> getLearningDataByCategoryNameAndMonth(Long userId, String categoryTitle,
            YearMonth month) {
        return categoryRepository.findByTitle(categoryTitle)
                .stream()
                .findFirst()
                .map(category -> getLearningDataByCategoryAndMonth(userId, category.getId(), month))
                .orElse(List.of()); // 見つからなかったら空リスト
    }
}