package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.*;
//import com.beyond.basic.b2_board.Repository.AuthorJdbcRepository;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 의존성주입 방법3
@Repository
// 스프링에서 '메서드단위'로 트랜잭션 처리(commit)를 하고, 만약 예외(unchecked)발생시 자동 롤백처리 지원.
@Transactional
public class AuthorService {
    //  private final AuthorMemoryRepository authorMemoryRepository;
    //  DB연결 후
    //  private final AuthorJdbcRepository authorRepository; (Jdbc)
    //  private final AuthorMyBatisRepository authorRepository; // (Mybatis)
    //  private final AuthorJpaRepository authorRepository;  // (JPA)

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;  //    암호화를 위한 의존성 주입
    private final PostRepository postRepository;    //    postCount를 위한 PostRepository 의존성 주입
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Client s3Client;

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

//   // 객체로 만들어지는 시점에 스프링에서 authorRepository 객체를 매개변수로 주입해줌
//   @Autowired
//    public AuthorService(AuthorMemoryRepository authorRepository) {
//        this.authorRepository = authorRepository; // 초기화 시켜줌
//    } // 이건 AuthorService의 생성자. 생성자는 AuthorService의 객체가 만들어질 때 생김

    // 의존성 주입 방법 3. RequiredArgs 어노테이션 사용 (이게 제일 간편함) -> 반드시 초기화 되어야 하는 필드(final 등)을 대상으로 생성자를 자동생성
    // 다형성 설계는 불가함. 어떤걸 주입해야 하는지 모르니까
// private final AuthorMemoryRepository authorMemoryRepository;
    public void save(AuthorCreateDto authorCreateDto, MultipartFile profileImage) {
//         이메일 중복검증
        if(authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        // 객체조립은 서비스 담당
        // Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        // toEntity패턴을 통해 Author객체 조립을 공통화
        String encodedPassword = passwordEncoder.encode(authorCreateDto.getPassword());     //암호화된 패스워드 만들기
        Author author = authorCreateDto.authorToEntity(encodedPassword);

//        cascading 테스트 : 회원이 생성될때, 곧바로 "가입인사"글을 생성하는 상황
//        방법1. 직접 POST객체 생성 후 저장
//        방법2. cascade옵션 활용
//        author.getPostList().add(post);
        this.authorRepository.save(author);

//        if(profileImage != null) {
////        image명 설정
//            String fileName = "user-" + author.getId() + "-profileimage-" + profileImage.getOriginalFilename();
//
////        저장 객체 구성
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(bucket)
//                    .key(fileName)
//                    .contentType(profileImage.getContentType())
//                    .build();
//
////        이미지를 업로드(byte형태로)
//            try {
//                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(profileImage.getBytes()));
//            } catch (Exception e) {
////            checked를 unchecked로 바꿔 전체 rollback되도록 예외처리
//                throw new IllegalArgumentException("이미지 업로드 실패");
//            }
//
////        이미지 url추출
//            String imgUrl = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(fileName)).toExternalForm();
//
//            author.updateImageUrl(imgUrl);
//        }
    }
    public Author doLogin(AuthorLoginDto dto) {
        Optional<Author> optionalAuthor = authorRepository.findByEmail(dto.getEmail());
        boolean check = true;

        if(!optionalAuthor.isPresent()) {
            check = false;
        } else {
//        비밀번호 일치여부 검증 : matches함수를 통해서 암호화되지않은값(dto의pw)을 다시 암호화하여 db의 password를 검증한다.
            if(!passwordEncoder.matches(dto.getPassword(), optionalAuthor.get().getPassword())) {
                check = false;
            }
        }
        if(!check) {
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        return optionalAuthor.get();
    }
//    readOnly = true : 롤백이 필요없는 경우
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

        // 위의 코드를 한줄로 가능 streamAPI 사용
        return authorRepository.findAll().stream()
            .map(a->a.listfromEntity()).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) throws NoSuchElementException {
       Author author = authorRepository.findById(id).orElseThrow(NoSuchElementException::new);
//       연관관계 설정없이 직접 조회해서 count값 찾는 경우
//       List<Post> postList = postRepository.findByAuthor(author);
//       AuthorDetailDto dto = AuthorDetailDto.fromEntity(author, postList.size()); // author 객체 넘겨줘야 함! this사용이 안됨.

//       OneToMany연관관계 설정을 통해 count값 찾는 경우
       AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return dto;
    }
    @Transactional(readOnly = true)
    public AuthorDetailDto myInfo() throws NoSuchElementException {
        String email = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        Author author = authorRepository
                        .findByEmail(email)
                        .orElseThrow(NoSuchElementException::new);
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return dto;
    }
    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        Author author = authorRepository.findByEmail(authorUpdatePwDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("없는 이메일 입니다."));
        // dirty checking : 객체를 수정한 후 별도의 update 쿼리 발생시키지 않아도, 영속성 컨텍스트에 의해 객체 변경사항 자동 DB반영
        author.updatePw(authorUpdatePwDto.getPassword());
    }
    public void delete(Long id){
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 아이디 입니다."));
        authorRepository.delete(author);
    }
 }

