package com.spring.springbootapplication.web;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeRecordFrom {
    private Long id;

    @NotNull(message = "学習時間は必ず入力してください")
    @Min(value = 0, message = "学習時間は0以上の数字で入力してください")
    private Integer timeRecord;

}
