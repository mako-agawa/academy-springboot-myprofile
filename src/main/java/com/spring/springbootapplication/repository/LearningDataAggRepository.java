package com.spring.springbootapplication.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.spring.springbootapplication.entity.LearningData;

public interface LearningDataAggRepository extends Repository<LearningData, Long> {

    interface TripleMonthSum {
        String getCategoryTitle();
        Integer getTwoMonthsAgo();
        Integer getLastMonth();
        Integer getCurrentMonth();
    }

    @Query("""
      select c.title as categoryTitle,
             sum(case when ld.learningDate >= :startTwo and ld.learningDate < :startLast
                      then ld.timeRecord else 0 end) as twoMonthsAgo,
             sum(case when ld.learningDate >= :startLast and ld.learningDate < :startCurr
                      then ld.timeRecord else 0 end) as lastMonth,
             sum(case when ld.learningDate >= :startCurr and ld.learningDate < :startNext
                      then ld.timeRecord else 0 end) as currentMonth
      from LearningData ld
      join ld.category c
      where ld.user.id = :userId
        and c.id = :categoryId
        and ld.learningDate >= :startTwo and ld.learningDate < :startNext
    """)
    TripleMonthSum sumForCategory3Months(
        @Param("userId") Long userId,
        @Param("categoryId") Long categoryId,
        @Param("startTwo") LocalDateTime startTwo,
        @Param("startLast") LocalDateTime startLast,
        @Param("startCurr") LocalDateTime startCurr,
        @Param("startNext") LocalDateTime startNext
    );
}