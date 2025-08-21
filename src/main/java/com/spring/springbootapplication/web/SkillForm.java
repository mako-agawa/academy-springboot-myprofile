package com.spring.springbootapplication.web;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillForm {
    @NotNull
    private Long id; // ← フィールド名とゲッター/セッターを id に統一

    @NotBlank(message = "スキル名は必ず入力してください")
    @Size(max = 50, message = "項目名は50文字以内で入力してください")
    @UniqueElements
    // "〜〜〜（入力した項目名）は既に登録されています"
    private String title;

    @NotBlank(message = "学習時間は必ず入力してください")
    @Size(min = 0, message = "学習時間は0以上の数字で入力してください")
    private int timeRecord;

    @Column(name = "learning_date", nullable = false)
    private LocalDateTime learningDate;

    private Long userId;
    private Long categoryId;

}