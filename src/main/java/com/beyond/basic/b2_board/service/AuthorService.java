package com.beyond.basic.b2_board.Service;

import com.beyond.basic.b2_board.Domain.Author;
import com.beyond.basic.b2_board.Dto.AuthorCreateDto;
import com.beyond.basic.b2_board.Dto.AuthorDetailDto;
import com.beyond.basic.b2_board.Dto.AuthorListDto;
import com.beyond.basic.b2_board.Dto.AuthorUpdatePwDto;
//import com.beyond.basic.b2_board.Repository.AuthorJdbcRepository;
import com.beyond.basic.b2_board.Repository.AuthorJpaRepository;
import com.beyond.basic.b2_board.Repository.AuthorMemoryRepository;
import com.beyond.basic.b2_board.Repository.AuthorMyBatisRepository;
import com.beyond.basic.b2_board.Repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // 실질적인 기능은 없음. 그래서 그냥 @Component로 해도 됨
// @Transactional 해야 함! (db하고나서..)
@RequiredArgsConstructor // 이거 방법3임
// 근데 트랜잭션 처리가 없는 경우에만 대체 가능.
@Repository
// 스프링에서 '메서드단위'로 트랜잭션 처리를 하고, 만약 예외(unchecked)발생시 자동 롤백처리 지원.
@Transactional // 예외처리를 잘 해놔야 의미가 있음.
public class AuthorService {

    //private final AuthorMemoryRepository authorMemoryRepository;
    // DB연결 후
    //  private final AuthorJdbcRepository authorRepository; (Jdbc)
    //    private final AuthorMyBatisRepository authorRepository; // (Mybatis)
//    private final AuthorJpaRepository authorRepository;  // (JPA)
    private final AuthorRepository authorRepository;
    // 객체를 가져다 쓰겠다.
    // 의존성 주입(DI) 방법1. Autowired 어노테이션 사용 -> 필드주입
//    @Autowired
//    private AuthorRepository authorRepository; => 얘는 구조적으로 다형성이 안됨. 여러개면 뭐인지를 모르기 때문
    ////    private final AuthorRepository authorRepository; // 재할당을 못하게 하려면 final
    ////    // 근데 여기서는 final이 에러남. final은 이 자리에서 초기화를 해줘야 하기 때문.
//    // 런타임때 싱글톤이 생성되니까 컴파일에서 에러가 나는 것


    // 의존성 주입(DI) 방법2. 생성자주입방식(가장많이 쓰는 방식)
    // 장점1) final을 통해 상수로 사용가능(안정성 향상) 2)다형성 구현가능 3)순환참조방지(컴파일타임에 check)
//    private final AuthorRepositoryInterface authorRepository;
//   // private final AuthorRepositoryInterface authorRepository = new AuthorRepository();
//   // 이거는 방법1의 @Autowired로는 안됨
//
//   // 객체로 만들어지는 시점에 스프링에서 authorRepository 객체를 매개변수로 주입해줌
    // 생성자가 하나밖에 없을 때에는 Autowired 생략 가능
//   @Autowired                                         여기에 주입!
//    public AuthorService(AuthorMemoryRepository authorRepository) {
//        this.authorRepository = authorRepository; // 초기화 시켜줌
//    } // 이건 AuthorService의 생성자. 생성자는 AuthorService의 객체가 만들어질 때 생김

    // 의존성 주입 방법 3. RequiredArgs 어노테이션 사용 (이게 제일 간편함) -> 반드시 초기화 되어야 하는 필드(final 등)을 대상으로 생성자를 자동생성
    // 다형성 설계는 불가함. 어떤걸 주입해야 하는지 모르니까
// private final AuthorMemoryRepository authorMemoryRepository; // 이렇게만 하면 오류남
    // 위에 @RequiredArgs
    //  여러개면 private final만 선언하면 됨 private final 어쩌구;

    public void save(AuthorCreateDto authorCreateDto) {
        // 이메일 중복검증
//        if(authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()){
//            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
//        }

        // AuthorRepository authorRepository = new AuthorRepository(); -> 이렇게 하면 메모리 낭비
        //this.authorRepository.save();

        // 객체조립은 서비스 담당
      //  Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword()); // 조립완료
        // toEntity패턴을 통해 Author객체 조립을 공통화
        Author author = authorCreateDto.authorToEntity();
        // setter가 author 엔티티에 없으므로, 우선 Author에 따로 생성자를 만들어줘야 함
        this.authorRepository.save(author);
    }

    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {
//        AuthorListDto authorListDto = new AuthorListDto();
//    List<AuthorListDto> dtoList = new ArrayList<>();
//    for (Author a : authorMemoryRepository.findAll()){
//        AuthorListDto dto //= new AuthorListDto(a.getId(), a.getName(), a.getEmail());
//        = a.listfromEntity();
//        dtoList.add(dto);
//    }
//        return dtoList;

        // 위에 코드를 한줄로 가능 streamAPI 사용
    return authorRepository.findAll().stream()
            .map(a->a.listfromEntity()).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) throws NoSuchElementException{ // Optional에서 꺼냄
       Author author = authorRepository.findById(id).orElseThrow(NoSuchElementException::new);
       // AuthorDetailDto dto = author.detailFromEntity(); -> 이렇게 domain에 DTO를 만들지 말고
        // DTO에 두자!
       AuthorDetailDto dto = AuthorDetailDto.fromEntity(author); // author 객체 넘겨줘야 함! this사용이 안됨.
//        AuthorDetailDto dto //= new AuthorDetailDto(author.getId(), author.getName(), author.getPassword());
//       = author.detailFromEntity(); => Author에 넣지 말기. 이거 잘못된 코드임
        return dto;

//        Optional<Author> optionalAuthor = authorMemoryRepository.findById(id);
//        return optionalAuthor.orElseThrow(()->new NoSuchElementException("없는 아이디 입니다."));

//        Author author = AuthorMemoryRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("없는 아이디 입니다."));
//        //orElseThrow 사용 Nosuch로
//        return author;

    }


//    public void updatePassword(String email, String newPassword) {
//        // setter가 없음 -> Author에 메서드 추가
//
//        Author author = authorMemoryRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("없는 이메일 입니다.")); // 리턴 타입은 Author 객체
//        author.updatePw(newPassword);
//    }
    // 위에 방법 말고 아예 객체를 받기
    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        Author author = authorRepository.findByEmail(authorUpdatePwDto.getEmail()) // 원본을 찾아옴
                .orElseThrow(() -> new IllegalArgumentException("없는 이메일 입니다."));
        // dirty checking : 객체를 수정한 후 별도의 update 쿼리 발생시키지 않아도, 영속성 컨텍스트에 의해 객체 변경사항 자동 DB반영
        author.updatePw(authorUpdatePwDto.getPassword());
    }


    public void delete(Long id){
        // id만 던지고 레포지토리에서 삭제해야 함
//        authorRepository.findById(id) // author에 있는거 쓰는게 아니므로 이렇게만 끝!
//                .orElseThrow(() -> new IllegalArgumentException("없는 이메일 입니다."));
//        authorRepository.delete(id);

               Author author = authorRepository.findById(id) // author에 있는거 쓰는게 아니므로 이렇게만 끝!
                .orElseThrow(() -> new IllegalArgumentException("없는 이메일 입니다."));
        authorRepository.delete(author);
    }

}

