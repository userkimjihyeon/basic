package com.beyond.basic.b1_hello.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
//    {name:"test", email:"test@naver", socores:[{subject:"수학", point:80}, {subject:"영어", point:90}]};
    private String name;
    private String email;
    private List<Score> scores;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Score {
        private String subject;
        private int point;
    }
}
