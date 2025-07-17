package com.beyond.basic.b2_board.Controller;

import com.beyond.basic.b2_board.Domain.Author;
import com.beyond.basic.b2_board.Dto.CommonDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/response/entity")
public class ResponseEntityController {

    // case1. @ResponseStatus 어노테이션 사용
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/annotation1")
    public String annotation1() {
        return "ok"; // 메시지는 ok. 200으로 올것임
        // 근데 201로 바꾸고 싶으면 ResponseEntity 사용할 수도 있으나.. ResponseStatus 사용하면 됨! 간편
        // ResponseStatus의 단점은 상태코드가 고정돼있음. 분기처리가 어려움
    }

    // case2. 메서드 체이닝 방식
    @GetMapping("/channing1")
    public ResponseEntity<?> channing1(){
        Author author = new Author("test", "test@naver.com", "1234");
        return ResponseEntity.status(HttpStatus.CREATED).body(author); // 빌더패턴(메서드가 이어짐)
    }

    // case3. ResponseEntity 객체를 직접 생성하는 방식(가장 많이 사용)
    @GetMapping("/custom1")
    public ResponseEntity<?> custom1(){
        Author author = new Author("test", "test@naver.com", "1234");
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    // 객체 하나 더 만들기
    /*
    {
    "result": {
        "id": 1,
        "name": "test",
        "email": "test@naver.com",
        "password": "1234"
    },
    "status_code": 201,
    "status_message": "author is created successfully"
}
     */
    @GetMapping("/custom2")
    public ResponseEntity<?> custom2(){
        Author author = new Author("test", "test@naver.com", "1234");
        return new ResponseEntity<>(new CommonDto(author, HttpStatus.CREATED.value(), "author is created successfully"), HttpStatus.CREATED);
    }

    }


