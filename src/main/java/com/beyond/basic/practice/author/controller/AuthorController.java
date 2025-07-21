//package com.beyond.basic.practice.author.controller;
//
//import com.beyond.basic.practice.author.dto.AuthorCreateDto;
//import com.beyond.basic.practice.author.dto.AuthorListDto;
//import com.beyond.basic.practice.author.dto.AuthorUpdatePwDto;
//import com.beyond.basic.practice.author.service.AuthorService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController("practiceAuthorController")
//@RequiredArgsConstructor
//public class AuthorController {
//    private final AuthorService authorService;
//
//    @PostMapping("/create")
//    public ResponseEntity<String> save(@Valid @RequestBody AuthorCreateDto authorCreateDto) {
//        this.authorService.save(authorCreateDto);
//        return new ResponseEntity<>("ok", HttpStatus.CREATED);
//    }
//    @GetMapping("/list")
//    public List<AuthorListDto> findAll() {
//        return authorService.findAll();
//    }
//    @GetMapping("/detail/{inputId}")
//    public ResponseEntity<?> findById(@PathVariable("inputId") Long inputId) {
//        return new ResponseEntity<>(authorService.findById(inputId), HttpStatus.OK);
//    }
//    @PatchMapping("/updatepw")
//    public void updatepw(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
//        authorService.updatePassword(authorUpdatePwDto);
//    }
//    @DeleteMapping("/delete/{inputId}")
//    public void delete(@PathVariable("inputId") Long inputId) {
//        this.authorService.delete(inputId);
//    }
//}
