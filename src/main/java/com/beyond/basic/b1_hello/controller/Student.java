package com.beyond.basic.b1_hello.controller;


/*
{name:"test", email:"test@naver", scores:[{subject:"수학", point:80}, {subject:"영어", point:90}]};
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student {
    private String name;
    private String email;
    private List<Score> scores;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class Score{
        private String subject;
        private int point;
    }


}