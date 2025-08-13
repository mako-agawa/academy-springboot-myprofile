package com.spring.springbootapplication.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.time.OffsetDateTime;
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
    @Size(max = 255, message = "氏名は255文字以内で入力してください")//文字数のバリデーション。
    @NotBlank(message = "名前は必ず入力してください")
    private String name;

    @Column(nullable = false, unique = true, length = 255)

    @NotBlank(message = "メールアドレスは必ず入力してください")
    @Email(message = "メールアドレスが正しい形式ではありません")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "メールアドレスが正しい形式ではありません")//メール形式のバリデーション。「＠や.」が含まれていないとバリデーションされる。
    private String email;

    @Column(name = "password_digest", nullable = false, length = 255)
    @NotBlank(message = "パスワードは必ず入力してください")
    @PasswordPolicy
    private String passwordDigest;

    @Column(columnDefinition = "text")
    private String biography;

    @Column(name = "thumbnail_url", length = 255)
    private String thumbnailUrl;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;
}