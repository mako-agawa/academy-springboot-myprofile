package com.spring.springbootapplication.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.service.LearningDataService;

public interface LearningDataRepository extends JpaRepository<LearningData, Long> {

 

    List<LearningData> findByUserId(Long userId);

    List<LearningData> findByCategoryId(Long categoryId);

    List<LearningData> findByUserIdAndCategoryId(Long userId, Long categoryId);

     // 今月分（開始日〜終了日）のデータを取る
    List<LearningData> findByUserIdAndLearningDateBetween(Long userId, LocalDateTime start, LocalDateTime end);

    List<LearningData> findByUserIdAndCategoryIdAndLearningDateBetween(
        Long userId, Long categoryId, LocalDateTime start, LocalDateTime end
    );

    
}
