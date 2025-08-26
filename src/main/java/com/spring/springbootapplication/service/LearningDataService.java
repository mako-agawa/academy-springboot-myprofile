package com.spring.springbootapplication.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.repository.CategoryRepository;
import com.spring.springbootapplication.repository.LearningDataRepository;

@Service
public class LearningDataService {
    private final LearningDataRepository learningDataRepository;
    private final CategoryRepository categoryRepository;

    // 作成日時の昇順（古い→新しい）で統一
    private static final Sort BY_CREATED_ASC = Sort.by(Sort.Direction.ASC, "createdAt");

    public LearningDataService(LearningDataRepository learningDataRepository,
                               CategoryRepository categoryRepository) {
        this.learningDataRepository = learningDataRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public LearningData save(LearningData data) {
        // createdAt は Entity の @PrePersist / @PreUpdate に任せる（上書きしない）
        return learningDataRepository.save(data);
    }

    /**
     * 新規保存専用（ユニークチェック付）
     * 既存更新のときは save() を使う想定（id != null の場合はユニークチェックをスキップ）
     */
    @Transactional
    public LearningData saveWithValidation(LearningData data) {
        if (data.getId() != null) {
            // 既存更新ならユニークチェックはスキップ（要件に合わせて調整）
            return learningDataRepository.save(data);
        }

        var range = monthRangeOf(data.getLearningDate());
        boolean exists = learningDataRepository.existsByUserIdAndTitleAndLearningDateBetween(
                data.getUser().getId(),
                data.getTitle(),
                range.start(),
                range.end());

        if (exists) {
            throw new IllegalArgumentException(data.getTitle() + "は既に登録されています");
        }
        return learningDataRepository.save(data);
    }

    /** 特定カテゴリ×指定月（createdAt 昇順で返す） */
    @Transactional(readOnly = true)
    public List<LearningData> getLearningDataByCategoryAndMonth(Long userId, Long categoryId, YearMonth month) {
        var range = monthRangeOf(month);
        return learningDataRepository.findByUserIdAndCategoryIdAndLearningDateBetween(
                userId, categoryId, range.start(), range.end(), BY_CREATED_ASC);
    }

    /** カテゴリ名指定×指定月（createdAt 昇順で返す） */
    @Transactional(readOnly = true)
    public List<LearningData> getLearningDataByCategoryNameAndMonth(Long userId, String categoryTitle, YearMonth month) {
        return categoryRepository.findByTitle(categoryTitle)
                .stream()
                .findFirst()
                .map(category -> getLearningDataByCategoryAndMonth(userId, category.getId(), month))
                .orElse(List.of());
    }

    /** ユーザー全体×指定月（createdAt 昇順で返す） */
    @Transactional(readOnly = true)
    public List<LearningData> getLearningDataByUserAndMonth(Long userId, YearMonth month) {
        var range = monthRangeOf(month);
        return learningDataRepository.findByUserIdAndLearningDateBetween(
                userId, range.start(), range.end(), BY_CREATED_ASC);
    }

    @Transactional(readOnly = true)
    public Optional<LearningData> findById(Long id) {
        return learningDataRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        learningDataRepository.deleteById(id);
    }

    // ===== ヘルパー =====

    private record Range(LocalDateTime start, LocalDateTime end) {}

    private static Range monthRangeOf(YearMonth ym) {
        return new Range(
            ym.atDay(1).atStartOfDay(),
            ym.atEndOfMonth().atTime(23, 59, 59)
        );
    }

    private static Range monthRangeOf(LocalDateTime anyDayInMonth) {
        YearMonth ym = YearMonth.from(anyDayInMonth);
        return monthRangeOf(ym);
    }
}