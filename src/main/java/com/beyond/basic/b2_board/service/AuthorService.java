package com.beyond.basic.b2_board.service;

import com.beyond.basic.b2_board.DTO.AuthorCreateDto;
import com.beyond.basic.b2_board.DTO.AuthorDetailDto;
import com.beyond.basic.b2_board.DTO.AuthorListDto;
import com.beyond.basic.b2_board.DTO.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//🚨@Transactional 붙여야함, rollback 추가설명🚨
@Service     //@Service는 @Component로 대체가능(transaction처리가 없는 경우에만)
@RequiredArgsConstructor
public class AuthorService {
////    ⭐⭐의존성 주입(DI)방법1. Autowired 어노테이션 사용 -> 필드주입 (단점: final불가, 다형성불가, 순환참조방지불가)
//    @Autowired
//    private AuthorRepository authorRepository;      //new가 필요없음. spring에서 만든 싱글톤repo객체를 가져다 쓰는 것.
////    ⭐⭐의존성 주입(DI)방법2. 생성자주입방식(가장 많이 사용)
////    장점1)final을 통해 상수로 사용가능(안정성향상) 장점2)다형성 구현가능    //순환참조(s<->r)방지(컴파일타임에 체크)
////                      인터페이스               객체          (+ implements) => 다형성 구현
//    private final AuthorMemoryRepository authorRepository;
    ////    객체(싱글톤)로 만들어지는 시점에 스프링에서 authorRepository객체를 매개변수로 주입.
//    @Autowired    //생성자가 1개밖에 없을때에는 @Autowired생략가능   -> 그냥 붙이셈
//    public AuthorService(AuthorMemoryRepository authorRepository) {   //생성자임.
//        this.authorRepository = authorRepository;
//    }
//    ⭐⭐의존성 주입(DI)방법3. @RequiredArgsConstructor 어노테이션 사용 -> 반드시 초기화 되어야 하는 필드(final 등)를 대상으로 생성자를 자동생성
//    다형성 설계는 불가
    private final AuthorMemoryRepository authorMemoryRepository;

//    객체조립은 서비스 담당
    public void save(AuthorCreateDto authorCreateDto) {
//    이메일 중복검증
        if (authorMemoryRepository.findByEmail(authorCreateDto.getEmail())
                .isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }
        this.authorMemoryRepository.save(new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword()));
    }
    //    ⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐author를 dto로 바꾸기
    public List<AuthorListDto> findAll() {
        List<AuthorListDto> dtoList = new ArrayList<>();
        for (Author a : authorMemoryRepository.findAll()) {
            AuthorListDto dto = new AuthorListDto(a.getId(), a.getName(), a.getEmail());
            dtoList.add(dto);
        }
        return dtoList;
    }
    public AuthorDetailDto findById(Long id) throws NoSuchElementException {        //가능한예외의 명시목적(기능X)
//        return this.authorMemoryRepository.findById(id).
//                orElseThrow(() -> new NoSuchElementException("존재하지 않는 id입니다."));
////        optional객체에서 꺼내는것도 service의 역할. (예외처리(controller) -> 예외터뜨리는것을 service에 하기 위함 -> spring에서 예외는 rollback의 기준)
//        Optional<Author> optionalAuthor = authorMemoryRepository.findById(id);
//        return optionalAuthor.orElseThrow(() -> new NoSuchElementException("존재하지 않는 id입니다."));
        Author author = authorMemoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 id입니다."));
        AuthorDetailDto dto = new AuthorDetailDto(author.getId(), author.getName(), author.getEmail());
        return dto;
    }
    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {   //service에서 authorUpdatePwDto를 email, password로 풀기.
//        setter용 method정의함.
        Author author = authorMemoryRepository.findByEmail(authorUpdatePwDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 email입니다."));
        author.updatePw(authorUpdatePwDto.getPassword());
    }
    public void delete(Long id) {
        // repo에서 id(사용자입력)에 해당하는 Author를 찾아 반환하고, 없으면 "없는 사용자입니다" 예외를 던짐
        authorMemoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
        authorMemoryRepository.delete(id);
    }


//////    ⭐⭐의존성 주입(DI)방법1. Autowired 어노테이션 사용 -> 필드주입 (단점: final불가, 다형성불가, 순환참조방지불가)
////    @Autowired
////    private AuthorRepository authorRepository;      //new가 필요없음. spring에서 만든 싱글톤repo객체를 가져다 쓰는 것.
//////    ⭐⭐의존성 주입(DI)방법2. 생성자주입방식(가장 많이 사용)
//////    장점1)final을 통해 상수로 사용가능(안정성향상) 장점2)다형성 구현가능    //순환참조(s<->r)방지(컴파일타임에 체크)
//////                      인터페이스               객체          (+ implements) => 다형성 구현
////    private final AuthorMemoryRepository authorRepository;
//////    객체(싱글톤)로 만들어지는 시점에 스프링에서 authorRepository객체를 매개변수로 주입.
////    @Autowired    //생성자가 1개밖에 없을때에는 @Autowired생략가능   -> 그냥 붙이셈
////    public AuthorService(AuthorMemoryRepository authorRepository) {   //생성자임.
////        this.authorRepository = authorRepository;
////    }
////    ⭐⭐의존성 주입(DI)방법3. @RequiredArgsConstructor 어노테이션 사용 -> 반드시 초기화 되어야 하는 필드(final 등)를 대상으로 생성자를 자동생성
////    다형성 설계는 불가
//    private final AuthorMemoryRepository authorMemoryRepository;
//
////    객체조립은 서비스 담당
//    public void save(AuthorCreateDto authorCreateDto) {
////    이메일 중복검증
//        if(this.authorMemoryRepository
//                .findByEmail(authorCreateDto.getEmail())
//                .isPresent()) {
//            throw new IllegalArgumentException("이미 존재하는 email입니다.");      //컨트롤러에서 try-catch
//        }
//        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
//        this.authorMemoryRepository.save(author);
//    }
////    ⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐author를 dto로 바꾸기
//    public List<AuthorListDto> findAll() {
//        List<AuthorListDto> dtoList = new ArrayList<>();
//        for (Author a : authorMemoryRepository.findAll()) {
//            AuthorListDto dto = new AuthorListDto(a.getId(), a.getName(), a.getEmail());
//            dtoList.add(dto);
//        }
//        return dtoList;
//    }
//    public AuthorDetailDto findById(Long id) throws NoSuchElementException {        //가능한예외의 명시목적(기능X)
////        return this.authorMemoryRepository.findById(id).
////                orElseThrow(() -> new NoSuchElementException("존재하지 않는 id입니다."));
//////        optional객체에서 꺼내는것도 service의 역할. (예외처리(controller) -> 예외터뜨리는것을 service에 하기 위함 -> spring에서 예외는 rollback의 기준)
////        Optional<Author> optionalAuthor = authorMemoryRepository.findById(id);
////        return optionalAuthor.orElseThrow(() -> new NoSuchElementException("존재하지 않는 id입니다."));
//        Author author = authorMemoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 id입니다."));
//        AuthorDetailDto dto = new AuthorDetailDto(author.getId(), author.getName(), author.getEmail());
//        return dto;
//    }
//    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {   //service에서 authorUpdatePwDto를 email, password로 풀기.
////        setter용 method정의함.
//        Author author = authorMemoryRepository.findByEmail(authorUpdatePwDto.getEmail())
//                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 email입니다."));
//        author.updatePw(authorUpdatePwDto.getPassword());
//    }
//    public void delete(Long id) {
//        // repo에서 id(사용자입력)에 해당하는 Author를 찾아 반환하고, 없으면 "없는 사용자입니다" 예외를 던짐
//        authorMemoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
//        authorMemoryRepository.delete(id);
//    }
}
