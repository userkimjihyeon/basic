package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    select * from post where author_id = ? and title = ?;     //?는 매개변수로 넘겨줘야함.
//    List<Post> findByAuthorIdAndTitle(Long authorId, String title);

//    select * from post where author_id = ? and title = ? order by createdTime desc;   //order by createdTime desc : 최신순 글정렬
//    List<Post> findByAuthorIdAndTitleOrderByCreatedTimeDesc(Long authorId, String title);

//    변수명은 author 지만 authorId로도 조회가능.
//    List<Post> findByAuthorId(Long authorId);
//    authorId 대신 객체 author로 넘기기
    List<Post> findByAuthor(Author author);

//    jpql을 사용한 일반 inner join
//    jpql은 기본적으로 lazy로딩을 지향하므로, inner join으로 filtering은 하되 post객체만 조회 -> N+1문제 여전히 발생
//    raw쿼리 : select ⭐p.*⭐ from Post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join p.author")
    List<Post> findAllJoin();

//    jpql을 사용한 fetch inner join
//    join시 post뿐만아니라 author객체까지 한꺼번에 조립하여 조회 -> N+1문제 해결
//    raw쿼리 : select ⭐*⭐ from Post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllFetchJoin();

//    paging처리 + delYn적용
//    org.springframework.data.domain.Pageable; 를 import
//    Page객체 안에 List<Post>포함, 전체페이지수 등의 정보 포함.
//    Pageable객체 안에는 페이지size, 페이지번호, 정렬기준 등이 포함.
//    resDto                                reqDto
    Page<Post> findAllByDelYnAndAppointment(Pageable pageable, String delYn, String appointment);  //Spring이 알아서 매칭

//    paging처리 + 검색(specification)
    Page<Post> findAll(Specification<Post> specification, Pageable pageable);

    List<Post> findByAppointment(String appointment);
}
