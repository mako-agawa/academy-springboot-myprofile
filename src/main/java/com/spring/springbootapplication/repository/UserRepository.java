// src/main/java/com/example/demo/category/CategoryRepository.java
package com.spring.springbootapplication.repository;

import com.spring.springbootapplication.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Long> { 
    Optional<User> findByEmail(String email);

    // 大文字小文字を区別したくない場合はこちらを使う
    boolean existsByEmailIgnoreCase(String email);
}