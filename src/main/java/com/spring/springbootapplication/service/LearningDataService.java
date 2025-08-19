package com.spring.springbootapplication.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.repository.LearningDataRepository;

@Service
public class LearningDataService {
    private final LearningDataRepository learningDataRepository;

    public LearningDataService(LearningDataRepository learningDataRepository) {
        this.learningDataRepository = learningDataRepository;
    }
    
    public List<LearningData> getLearningDataByCategoryAndMonth(Long userId, Long categoryId, YearMonth month) {
        LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
        return learningDataRepository.findByUserIdAndCategoryIdAndLearningDateBetween(
                userId, categoryId, startOfMonth, endOfMonth);
    }
}