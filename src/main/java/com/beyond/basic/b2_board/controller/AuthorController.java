package com.beyond.basic.b2_board.controller;

import com.beyond.basic.b2_board.DTO.AuthorCreateDto;
import com.beyond.basic.b2_board.DTO.AuthorDetailDto;
import com.beyond.basic.b2_board.DTO.AuthorListDto;
import com.beyond.basic.b2_board.DTO.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.service.AuthorService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//컨트롤러계층 : http req(사용자요청)분석, res생성
@RestController     //@Controller + @ResponseBody(화면return) : 화면return 불가. 데이터만 전송.
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;  //service주입
    //    ⭐CRUD⭐
    //    회원가입
    @PostMapping("/create")
    public String save(@RequestBody AuthorCreateDto authorCreateDto) {
        try {
            this.authorService.save(authorCreateDto);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "OK";
    }
    //    회원목록조회 : /list
    @GetMapping("/list")
    public List<AuthorListDto> findAll() {
        return this.authorService.findAll();
    }
    //    회원상세조회(id로 조회) : /detail/1
//    서버에서 별도의 try-catch를 하지않으면, 에러발생시 500에러 + 스프링의 포맷으로 에러 리턴.
    @GetMapping("/detail/{id}")
    public AuthorDetailDto findById(@PathVariable Long id) {
        try {
            return this.authorService.findById(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    //    비밀번호수정(email, password 사용 -> json) : /updatepw
//    http주요메서드 : get:조회, post:등록, patch:부분수정, put:대체, delete:삭제
    @PatchMapping("/updatepw")
    public String updatePw(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
        this.authorService.updatePassword(authorUpdatePwDto);
        return "OK";
    }
    //    회원탈퇴(삭제) : /delete/1
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.authorService.delete(id);
        return "OK";
    }


//    private final AuthorService authorService;  //service주입
////    ⭐CRUD⭐
////    회원가입
//    @PostMapping("/create")
//    public String save(@RequestBody AuthorCreateDto authorCreateDto) {
//        try {
//            this.authorService.save(authorCreateDto);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            return e.getMessage();
//        }
//        return "OK";
//    }
////    회원목록조회 : /list
//    @GetMapping("/list")
//    public List<AuthorListDto> findAll() {
//        return this.authorService.findAll();
//    }
////    회원상세조회(id로 조회) : /detail/1
////    서버에서 별도의 try-catch를 하지않으면, 에러발생시 500에러 + 스프링의 포맷으로 에러 리턴.
//    @GetMapping("/detail/{id}")
//    public AuthorDetailDto findById(@PathVariable Long id) {
//        try {
//            return this.authorService.findById(id);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
////    비밀번호수정(email, password 사용 -> json) : /updatepw
////    http주요메서드 : get:조회, post:등록, patch:부분수정, put:대체, delete:삭제
//    @PatchMapping("/updatepw")
//    public String updatePw(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
//        this.authorService.updatePassword(authorUpdatePwDto);
//        return "OK";
//    }
////    회원탈퇴(삭제) : /delete/1
//    @DeleteMapping("/delete/{id}")
//    public String delete(@PathVariable Long id) {
//        this.authorService.delete(id);
//        return "OK";
//    }
}
