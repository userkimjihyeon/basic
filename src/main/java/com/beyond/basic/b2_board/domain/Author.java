package com.beyond.basic.b2_board.Domain;

import com.beyond.basic.b2_board.Dto.AuthorDetailDto;
import com.beyond.basic.b2_board.Dto.AuthorListDto;
import com.beyond.basic.b2_board.Repository.AuthorMemoryRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
// JPA를 사용할 경우, Entity 반드시 붙여야하는 어노테이션
// JPA의 EntityManager에게 객체를 위임하기 위한 어노테이션
// 엔티티매니저는 영속성컨텍스트(엔티티의 현재상황)를 통해 db 데이터 관리.
// 코드(객체)를 우선시함
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// pk를 설정하겠다는 뜻
    // identity : auto_increment, auto : id생성 전략을 jpa에게 자동설정하도록 위임하는 것
    private Long id;
    // 컬럼에 별다른 설정이 없을경우 default varchar(255)
    private String name;
    @Column(length=50,  unique = true, nullable=false)
    private String email;

    // @Column(name = "pw") : 되도록이면 컬럼명과 변수명을 일치시키는 것이 개발의 혼선을 줄일 수 있음.
    private String password;
//    컬럼명에 캐멀케이스 사용시, db에는 created time으로 컬럼 생성
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updatedTime;
//    private String test;
//    private String test2;

    // 별도 생성자를 만듦
    public Author(String name, String email, String password) {
     //   this.id= AuthorMemoryRepository.id; // static이라 이렇게 쓰는 것
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updatePw(String password){
      this.password = password;
    }

    // to 엔티티와 from엔티티 모두 dto에 만들기

    // 밑에껏도 dto에 넣어야 함... 우선은 여기에 두겠음 수정필요!
    public AuthorDetailDto detailFromEntity(){ // 리턴타입을 Dto로
        return new AuthorDetailDto(this.id, this.name, this.email); // this는 Author임
    }

    public AuthorListDto listfromEntity(){
        return new AuthorListDto(this.id, this.name, this.email);
    }
}
