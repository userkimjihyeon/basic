package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorMemoryRepository {
//    authorList는 계속 author객체가 적재되어야하는데 static으로 만들지 않는 이유
//    -> main(@SpringBootApplication)에서 @Repository(@Component)로 싱글톤객체를 생성함!
    private List<Author> authorList = new ArrayList<>();    //나중엔 이거 db에 저장할 것.
    public static Long id = 1L;

    //    회원가입
    public void save(Author author) {
        authorList.add(author);
        id++;
    }
    //    회원목록조회
    public List<Author> findAll() {
        return this.authorList;
    }
    //    회원상세조회1. id
    public Optional<Author> findById(Long id) {
        Author author = null;
        for (Author a : this.authorList) {
            if(a.getId().equals(id)) {
                author = a;
            }
        }
        return Optional.ofNullable(author);
//        return authorList.stream().filter(a -> a.getId().equals(id)).findFirst();
    }
    //    회원상세조회2. email
    public Optional<Author> findByEmail(String email) {
        return authorList.stream().filter(a -> a.getEmail().equals(email)).findFirst();
    }
    //    회원탈퇴
    public void delete(Long id) {
//        repo접근 -> list삭제처리. id값으로 요소와 index(i)값을 찾아 처리. authorList.remove(index);
        for(int i=0; i<this.authorList.size(); i++) {
            if(this.authorList.get(i).getId().equals(id)) {
                this.authorList.remove(i);
                break;
            }
        }
    }

//    비밀번호수정 -> service계층에서

////    authorList는 계속 author객체가 적재되어야하는데 static으로 만들지 않는 이유
////    -> main(@SpringBootApplication)에서 @Repository(@Component)로 싱글톤객체를 생성함!
//    private List<Author> authorList = new ArrayList<>();    //나중엔 이거 db에 저장할 것.
//    public static Long id = 1L;
//
////    회원가입
//    public void save(Author author) {
//        this.authorList.add(author);
//        id++;
//    }
////    회원목록조회
//    public List<Author> findAll() {
//        return this.authorList;
//    }
////    회원상세조회1. id
//    public Optional<Author> findById(Long id) {
//        Author author = null;
//        for (Author a : this.authorList) {
//            if(a.getId().equals(id)) {
//                author = a;
//            }
//        }
//        return Optional.ofNullable(author);
////        return authorList.stream().filter(a -> a.getId().equals(id)).findFirst();
//    }
////    회원상세조회2. email
//    public Optional<Author> findByEmail(String email) {
//        return authorList.stream().filter(a -> a.getEmail().equals(email)).findFirst();
//    }
////    회원탈퇴
//    public void delete(Long id) {
////        repo접근 -> list삭제처리. id값으로 요소와 index(i)값을 찾아 처리. authorList.remove(index);
//        for(int i=0; i<this.authorList.size(); i++) {
//            if(this.authorList.get(i).getId().equals(id)) {
//                this.authorList.remove(i);
//                break;
//            }
//        }
//    }
////    비밀번호수정 -> service계층에서
}
