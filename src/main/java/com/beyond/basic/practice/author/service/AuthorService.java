package com.beyond.basic.practice.author.service;

import com.beyond.basic.practice.author.domain.Author;
import com.beyond.basic.practice.author.dto.AuthorCreateDto;
import com.beyond.basic.practice.author.dto.AuthorDetailDto;
import com.beyond.basic.practice.author.dto.AuthorListDto;
import com.beyond.basic.practice.author.dto.AuthorUpdatePwDto;
import com.beyond.basic.practice.author.repository.AuthorRepository;
import com.beyond.basic.practice.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service // 실질적인 기능은 없음. 그래서 그냥 @Component로 해도 됨
@RequiredArgsConstructor // 이거 방법3임
@Repository
@Transactional // 예외처리를 잘 해놔야 의미가 있음.
public class AuthorService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    public void save(AuthorCreateDto authorCreateDto) {
        Author author = authorCreateDto.authorToEntity();
        this.authorRepository.save(author);
    }
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {
        return authorRepository.findAll().stream()
            .map(a->a.listFromEntity()).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) throws NoSuchElementException{ // Optional에서 꺼냄
       Author author = authorRepository.findById(id)
               .orElseThrow(NoSuchElementException::new);
       AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return dto;
    }
    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        Author author = authorRepository.findByEmail(authorUpdatePwDto.getEmail()) // 원본을 찾아옴
                .orElseThrow(() -> new IllegalArgumentException("없는 이메일 입니다."));
        author.updatePw(authorUpdatePwDto.getPassword());
    }
    public void delete(Long id){
        Author author = authorRepository.findById(id) // author에 있는거 쓰는게 아니므로 이렇게만 끝!
                .orElseThrow(() -> new IllegalArgumentException("없는 아이디 입니다."));
        authorRepository.delete(author);
    }
}

