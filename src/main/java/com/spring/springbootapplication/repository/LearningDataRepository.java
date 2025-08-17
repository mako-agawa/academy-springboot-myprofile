package com.spring.springbootapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.springbootapplication.entity.LearningData;

public interface LearningDataRepository extends JpaRepository<LearningData, Long> {
    List<LearningData> findByUserId(Long userId);

    List<LearningData> findByCategoryId(Long categoryId);

    List<LearningData> findByUserIdAndCategoryId(Long userId, Long categoryId);
}
