package com.beyond.basic.b2_board.author.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonDto {
    private Object result; // 어떤 객체가 올지 모르므로
    private int status_code;
    private String status_message;
}
