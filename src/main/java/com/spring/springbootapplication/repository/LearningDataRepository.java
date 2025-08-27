package com.spring.springbootapplication.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.springbootapplication.entity.LearningData;

public interface LearningDataRepository extends JpaRepository<LearningData, Long> {

        boolean existsByUserIdAndTitleAndLearningDateBetween(
                        Long userId, String title, LocalDateTime start, LocalDateTime end);

        List<LearningData> findByUserId(Long userId);

        List<LearningData> findByCategoryId(Long categoryId);

        List<LearningData> findByUserIdAndCategoryId(Long userId, Long categoryId);

        List<LearningData> findByUserIdAndLearningDateBetween(Long userId, LocalDateTime start, LocalDateTime end);

        List<LearningData> findByUserIdAndCategoryIdAndLearningDateBetween(
                        Long userId, Long categoryId, LocalDateTime start, LocalDateTime end);

        List<LearningData> findByUserIdAndCategoryId(
                        Long userId, Long categoryId, Sort sort);

        List<LearningData> findByUserIdAndLearningDateBetween(
                        Long userId, LocalDateTime start, LocalDateTime end, Sort sort);

        List<LearningData> findByUserIdAndCategoryIdAndLearningDateBetween(
                        Long userId, Long categoryId, LocalDateTime start, LocalDateTime end, Sort sort);
                        
        @EntityGraph(attributePaths = "category")
        List<LearningData> findByUserIdAndLearningDateBetween(
                        Long userId,
                        LocalDate start,
                        LocalDate end);

}
