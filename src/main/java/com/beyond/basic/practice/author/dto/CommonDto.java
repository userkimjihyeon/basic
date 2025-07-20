package com.beyond.basic.practice.author.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonDto {
    private Object result;
    private int status_code;
    private String status_message;
}
