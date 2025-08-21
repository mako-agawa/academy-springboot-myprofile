// src/main/java/com/example/demo/category/CategoryRepository.java
package com.spring.springbootapplication.repository;

import com.spring.springbootapplication.entity.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByTitle(String tite);

}