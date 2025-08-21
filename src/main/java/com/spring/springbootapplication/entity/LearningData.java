package com.spring.springbootapplication.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "learning_data")
public class LearningData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 25)
    @Size(max = 25, message = "氏名は25文字以内で入力してください") // 文字数のバリデーション。
    @NotBlank(message = "名前は必ず入力してください")
    private String title;

    @Column(name = "time_record", nullable = false)    
    private int timeRecord;

    @Column(name = "learning_date", nullable = false)
    private LocalDateTime learningDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference("user-learning")
    private User user;

    @JsonBackReference("category-learning")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", insertable = false, updatable = true)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist // ← PerPersist ではなく PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now(); // ← now() はメソッド呼び出し
        updatedAt = createdAt; // 作成時は同じ値にしておく
    }

    @PreUpdate // ← PerUpdate ではなく PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
