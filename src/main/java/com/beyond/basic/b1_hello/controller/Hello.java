package com.beyond.basic.b1_hello.controller;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

//@Getter               //class내의 모든 변수를 대상으로 getter가 생성됨.
@Data                   //getter, setter, tostring메서드까지 모두 만들어주는 어노테이션
@AllArgsConstructor     //모든 매개변수가 있는 생성자를 생성하는 어노테이션
@NoArgsConstructor      //기본생성자(매개변수가 없음)
//기본생성자+getter로 parsing이 이루어지므로, 보통은 필수적 요소.
public class Hello {
    private String name;
    private String email;
//    private MultipartFile photo;
}
