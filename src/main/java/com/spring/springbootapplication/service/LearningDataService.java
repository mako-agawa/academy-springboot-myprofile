package com.spring.springbootapplication.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

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

    public LearningData save(LearningData data) {
        return learningDataRepository.save(data);
    }
    
    
    // 保存用メソッド（ユニークチェック付き）
    public LearningData saveWithValidation(LearningData data) {
        LocalDateTime startOfMonth = data.getLearningDate()
                .withDayOfMonth(1)
                .toLocalDate()
                .atStartOfDay();
        LocalDateTime endOfMonth = data.getLearningDate()
                .withDayOfMonth(data.getLearningDate().toLocalDate().lengthOfMonth()).toLocalDate()
                .atTime(23, 59, 59);

        System.out.println("Checking for existing data in the month: " + startOfMonth + " to " + endOfMonth);
        boolean exists = learningDataRepository.existsByUserIdAndTitleAndLearningDateBetween(
                data.getUser().getId(),
                data.getTitle(),
                startOfMonth,
                endOfMonth);

        if (exists) {
            throw new IllegalArgumentException("同じ月に同じスキル名は登録できません");
        }

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
                .orElse(List.of());
    }

    public List<LearningData> getLearningDataByUserAndMonth(Long userId, YearMonth month) {
        LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
        return learningDataRepository.findByUserIdAndLearningDateBetween(
                userId, startOfMonth, endOfMonth);
    }

    public Optional<LearningData> findById(Long id) {
        return learningDataRepository.findById(id);
    }

    public void deleteById(Long id) {
        learningDataRepository.deleteById(id);
    }
}