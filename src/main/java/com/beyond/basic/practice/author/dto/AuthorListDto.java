package com.beyond.basic.practice.author.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorListDto {
    private Long id;
    private String name;
    private String email;
}
