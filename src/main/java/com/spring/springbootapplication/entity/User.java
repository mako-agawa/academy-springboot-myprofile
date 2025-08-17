package com.spring.springbootapplication.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

import com.spring.springbootapplication.validation.PasswordPolicy;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 255)
    @Size(max = 255, message = "氏名は255文字以内で入力してください") // 文字数のバリデーション。
    @NotBlank(message = "名前は必ず入力してください")
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    @NotBlank(message = "メールアドレスは必ず入力してください")
    // @Email(message = "メールアドレスが正しい形式ではありません")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "メールアドレスが正しい形式ではありません") // メール形式のバリデーション。「＠や.」が含まれていないとバリデーションされる。
    private String email;

    @Column(name = "password_digest", nullable = false, length = 255)
    @NotBlank(message = "パスワードは必ず入力してください")
    @PasswordPolicy
    private String passwordDigest;

    @Column(columnDefinition = "text")
    private String biography;

    /** Cloudinary の public_id を保存（例: avatars/123/abc-def） */
    @Column(name = "thumbnail_public_id", length = 255)
    private String thumbnailPublicId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LearningData> learningDatas;

}