package com.beyond.basic.b2_board.author.controller;

// 스프링은 레포 -> 서비스 -> 컨트롤러 순으로 만듦

import com.beyond.basic.b2_board.author.dto.*;
import com.beyond.basic.b2_board.author.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.author.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.author.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller + ResponseBody
@RestController // Component로 대체 불가능. Controller는 자체만으로 기능이 많아서
@RequiredArgsConstructor // 의존성 주입방법 3번째

@RequestMapping("/author")
public class AuthorController {
    // 서비스 주입받기
    private final AuthorService authorService;

//    public AuthorController(AuthorMemoryRepository authorMemoryRepository) {
//        this.authorMemoryRepository = authorMemoryRepository;
//    } // @RequiredArgsConstructor 이거 쓰면 생성자 안써도 됨


    // 회원가입
    // 이렇게 Entity 자체를 @RequestBody의 매개변수에 넣지 않음 -> 회원가입 전송용도로 따로 Dto 임
    // public void save(@RequestBody Author author){} // json형식으로. 그래서 RequestBody 사용
    // 사용자의 입력값과 Author에 있는 요소들이 다를 수 있음. 모든 메서드의 입력값이 다르므로.
    @PostMapping("/create")
//    public String save(@RequestBody AuthorCreateDto authorCreateDto) {
//        // 가입할 때는 id를 회원이 입력할 수는 없으니까 따로 DTO생성
//        try{
//            this.authorService.save(authorCreateDto);
//        } catch(IllegalArgumentException e){
//            e.printStackTrace();
//            return e.getMessage();
//        }
//        return "ok";
//    }

//    // 응답코드 제대로 설정
//        public ResponseEntity<String> save(@RequestBody AuthorCreateDto authorCreateDto) {
//        // 가입할 때는 id를 회원이 입력할 수는 없으니까 따로 DTO생성
//        try{
//            this.authorService.save(authorCreateDto);
//           return new ResponseEntity<>("ok", HttpStatus.CREATED);
//        } catch(IllegalArgumentException e){
//            e.printStackTrace();
//            // 생성자 매개변수 body 부분의 객체와 header부에 상태코드를 넣는다.
//            // 상태코드는 정해져있는 것에 맞춰서 해야 함                                     <---사용자의 잘못된 요청--->
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
    // ControllerAdvice가 없었으면 위와 같이 개별적인 예외처리가 필요하나, 이제는 전역적인 예외처리가 가능.
//    @Valid : dto의 validation어노테이션과 controller의 @Valid가 한쌍.
    public ResponseEntity<String> save(@Valid @RequestBody AuthorCreateDto authorCreateDto) {
        this.authorService.save(authorCreateDto);
        return new ResponseEntity<>("ok",  HttpStatus.CREATED);
    }

    // 회원목록조회 : /author/list
    @GetMapping("/list")
//    public List<Author> findAll(){
//       return this.authorService.findAll();
//    }
    public List<AuthorListDto> findAll(){
        return authorService.findAll();
    }

    // 회원상세조회 : id로 조회 : /author/detail/1 (PathVariable방식)
    // 서버에서 별도의 try catch를 하지 않으면, 에러 발생시 500에러 + 스프링의 포맷으로 에러 리턴.
    // 없는 아이디로 보내면 그건 원래는 사용자가 잘못 입력한 것이므로 400에러인건데, 스프링에서는 그냥 500으로 띄움

    @GetMapping("/detail/{inputId}")
//    public AuthorDetailDto findById(@PathVariable("inputId") Long inputId){
//        try{
//            return this.authorService.findById(inputId); // JSON으로 만드는 것. 객체를 리턴하는데 @RestController가 있으므로 자동으로 json으로 변환됨!
//        }catch(IllegalArgumentException e){
//            e.printStackTrace(); // 이건 에러를 터뜨린게 아님. 사실상 그냥 문자열 전달
//            // 그러다보니 에러가 나도 응답코드가 200으로 감..
//        }
//        return null;
//    }
    public ResponseEntity<?> findById(@PathVariable("inputId") Long inputId){
//        try{
//            //                                  <---------객체넣기(Author)------------->
//            return new ResponseEntity<>(authorService.findById(inputId), HttpStatus.OK);
//        } catch(NoSuchElementException e){
//            e.printStackTrace();             //<--String----> ? 여서 가능
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
//        }
//    }
        // CommonDto로 만들어보기⭐
//        try{
//            return new ResponseEntity<>(new CommonDto(authorService.findById(inputId), HttpStatus.CREATED.value(), "author is created successfully"), HttpStatus.CREATED);
//        }catch(NoSuchElementException e){
//            e.printStackTrace();
//            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), "author is not found"), HttpStatus.BAD_REQUEST);
//        }
        return new ResponseEntity<>(authorService.findById(inputId), HttpStatus.OK);
    }
        //return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), "author is not found"), HttpStatus.BAD_REQUEST);


        // 비밀번호수정 : email, password -> json : /author/updatepw
        // 수정은 딱히 응답을 줄 게 없어서 void
        // put : 대체, patch : 부분수정
        // get : 조회, post : 등록, patch : 부분수정, put : 대체(완전히 갈아끼울 때), delete : 삭제
        @PatchMapping("/updatepw")
        public void updatePw (@RequestBody AuthorUpdatePwDto authorUpdatePwDto){
            authorService.updatePassword(authorUpdatePwDto);
        }
//    public void updatePw(@RequestParam String email, @RequestParam String password){
//        this.authorService.updatePw(email,password);
//    }



    // 회원탈퇴(삭제) : /author/delete/1
    @DeleteMapping("/delete/{inputId}")
    public void delete(@PathVariable("inputId") Long inputId){
        this.authorService.delete(inputId);
    }

}
