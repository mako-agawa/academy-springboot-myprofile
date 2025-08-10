// src/main/java/com/example/demo/category/CategoryRepository.java
package com.spring.springbootapplication.repository;

import com.spring.springbootapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> { }