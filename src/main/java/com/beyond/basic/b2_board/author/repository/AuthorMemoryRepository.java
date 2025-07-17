package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.stereotype.Repository;
// 나중에 JPA는 리턴타입이 정해져 있음
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository // 싱글톤으로 만들어줌(그럼 static 효과). 이 안에 @Component 있음
// public class AuthorMemoryRepository implements AuthorRepositoryInterface  { // static으로 만들지 않아도 됨
    public class AuthorMemoryRepository { // static으로 만들지 않아도 됨
    private List<Author> authorList = new ArrayList<>();
    public static Long id = 1L;

    // 회원가입
    public void save(Author author){
        this.authorList.add(author);
        id++;
    }

    // 회원목록
    public List<Author> findAll(){
        return this.authorList;
    }
// 얘는 초기화가 돼있어서 list에 아무것도 없어도 null 에러 안남!

    // 회원 아이디로 찾기
    public Optional<Author> findById(Long id){
        Author author = null; // 이건 사이즈 하면 null에러남
        for(Author a : this.authorList){
            if(a.getId()==id){
                author =a;
            }
        }
        return Optional.ofNullable(author);
    }

    // 회원 이메일로 찾기
    public Optional<Author> findByEmail(String email){
        Author author = null;
        for(Author a : this.authorList){
            if(a.getEmail().equals(email)){
                author =a;
            }
        }
        return Optional.ofNullable(author);
    }

    // 회원 삭제
    public void delete(Long id){
        // id값으로 요소의 index값을 찾아 삭제
        for(int i=0; i<this.authorList.size(); i++){
            if(this.authorList.get(i).getId().equals(id)){
                this.authorList.remove(i);
                break;
            }
        }
    }
    // 회원정보 수정은 레퍼지토리에 없어도 됨. id또는 email로 찾아서 Service에서
}
