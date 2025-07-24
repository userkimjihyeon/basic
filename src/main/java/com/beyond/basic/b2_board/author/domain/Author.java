package com.beyond.basic.b2_board.author.domain;

import com.beyond.basic.b2_board.author.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.common.BaseTimeEntity;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
// JPA를 사용할 경우, Entity 반드시 붙여야하는 어노테이션
// JPA의 EntityManager에게 객체를 위임하기 위한 어노테이션
// 엔티티매니저는 영속성컨텍스트(엔티티의 현재상황)를 통해 db 데이터 관리.
// 코드(객체)를 우선시함
@Entity
// @Builder를 통해 유연하게 객체 생성가능
@Builder
public class Author extends BaseTimeEntity {
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
    @Enumerated(EnumType.STRING)
    @Builder.Default //빌더패턴에서 변수 초기화(디폴트값)시 Builder.Default어노테이션 필수
    private Role role = Role.USER;
    private String profileImage;

//    OneToMany는 선택사항. 또한 default가 lazy.
//    mappedBy에는 ManyToOne쪽(post)의 변수명을 문자열로 지정. fk관리를 반대편쪽(post)에서 한다는 의미 -> 연관관계의 주인설정
//    cascade : 부모객체의 변화에 따라 자식객체가 같이 변하는 옵션 1)persist : 저장 2)remove : 삭제
//    자식의 자식까지 모두 삭제할경우 orphanRemoval = true 옵션 추가. (ex. post삭제 시, 댓글도 삭제할때)
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<Post> postList = new ArrayList<>();    //@OneToMany설정시, 초기화 필수

    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)  //mappedBy = "author": Address쪽의 변수명
    private Address address;

////    컬럼명에 캐멀케이스 사용시, db에는 created time으로 컬럼 생성
//    @CreationTimestamp
//    private LocalDateTime createdTime;
//    @UpdateTimestamp
//    private LocalDateTime updatedTime;

//    // 별도 생성자를 만듦
//    public Author(String name, String email, String password) {
//     //   this.id= AuthorMemoryRepository.id; // static이라 이렇게 쓰는 것
//        this.name = name;
//        this.email = email;
//        this.password = password;
//    }
//
//    public Author(String name, String email, String password, Role role) {
//     //   this.id= AuthorMemoryRepository.id; // static이라 이렇게 쓰는 것
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.role = role;
//    }

    public void updatePw(String password){
        this.password = password;
    }
    public AuthorListDto listfromEntity(){
        return new AuthorListDto(this.id, this.name, this.email);
    }
    public void updateImageUrl(String imgUrl) {
        this.profileImage = imgUrl;
    }
}
