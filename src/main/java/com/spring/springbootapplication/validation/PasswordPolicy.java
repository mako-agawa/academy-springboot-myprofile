package com.spring.springbootapplication.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Size(min = 8, message = "英数字8文字以上で入力してください")
@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "英字と数字を両方含めてください")
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {}) // 合成だけなら空でOK
@Documented
@ReportAsSingleViolation // ← 1つのメッセージにまとめたい場合は付ける
public @interface PasswordPolicy {
    String message() default "英数字8文字以上で入力してください";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}