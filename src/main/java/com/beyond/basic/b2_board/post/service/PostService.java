package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.dto.PostSearchDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
//@RequiredArgsConstructor          //대신 constructor추가 및 @autowired
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();    //claims의 subject: email
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 id입니다."));
        LocalDateTime appointmentTime = null;
        if (dto.getAppointment().equals("Y")) {
            if(dto.getAppointmentTime() == null || dto.getAppointmentTime().isEmpty()) {
                throw new IllegalArgumentException("시간정보가 비워져 있습니다.");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appointmentTime = LocalDateTime.parse(dto.getAppointmentTime(), dateTimeFormatter);
        }

        postRepository.save(dto.toEntity(author, appointmentTime));
    }
    public Page<PostListDto> findAll(Pageable pageable, PostSearchDto dto) {
//        N+1문제
//        List<Post> postList = postRepository.findAll();                 //일반 전체조회
//        List<Post> postList = postRepository.findAllJoin();             //일반 inner join
//        List<Post> postList = postRepository.findAllFetchJoin();        //inner join fetch
//        postList를 조회할때 참조관계에 있는 author까지 조회하게 되므로, N(author쿼리)+1(post쿼리)문제 발생
//        jpa는 기본방향성이 fetch lazy이므로, 참조하는시점에 쿼리를 내보내게 되어 JOIN문을 만들어주지 않고(직접 join쿼리 생성), N+1문제 발생 (->fetch join 사용해야함)

//        검색을 위해 Specification 객체를 스프링에서 제공
//        Specification객체는 복잡한 쿼리를 명세를 이용하여 정의하는 방식으로, 쿼리를 쉽게 생성.
//        페이지처리 findAll호출
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                Root : 엔티티의 속성을 접근하기 위한 객체, CriteriaBuilder : 쿼리를 생성하기 위한 객체
                List<Predicate> predicateList = new ArrayList<>();
//                select * from post where del_yn="N" and appointment="N" and title like "%inputTitle%" and category=inputCategory
//                정적
                predicateList.add(criteriaBuilder.equal(root.get("delYn"), "N"));
                predicateList.add(criteriaBuilder.equal(root.get("appointment"), "N"));
//                동적
                if(dto.getCategory() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), dto.getCategory()));
                }
                if(dto.getTitle() != null) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%" + dto.getTitle() + "%"));
                }
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                for(int i=0; i<predicateList.size(); i++) {
                    predicateArr[i] = predicateList.get(i);
                }
//                위의 검색 조건들을 하나(한줄)의 Predicate객체로 만들어서 return
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };
        Page<Post> postList = postRepository.findAll(specification, pageable);  //specification 검색, pageable 페이징처리
//        return postList.stream().map(a -> PostListDto.fromEntity(a)).collect(Collectors.toList());
        return postList.map(a -> PostListDto.fromEntity(a));    //list객체가 아니라 Page객체이므로 간소해짐.
    }
    public PostDetailDto findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 id입니다."));
//        case1. 엔티티간의 관계성 설정을 하지 않았을때
//        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
//        return PostDetailDto.fromEntity(post, author);
//        case2. 엔티티간의 관계성 설정을 통해 Author객체를 쉽게 조회하는 경우
        return PostDetailDto.fromEntity(post);
    }
}
