package com.beyond.basic.b2_board.common;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//CommandLineRunner를 구현함으로써 해당 컴포넌트가 스프링빈으로 등록되는 시점에 run메서드 자동실행
@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
//        admin@naver.com 이미 존재하면 return으로 종료.
        if(authorRepository.findByEmail("admin@naver.com").isPresent()) {
            return;
        }
        Author author = Author.builder()
                .email("admin@naver.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("12341234"))
                .build();
        authorRepository.save(author);
    }
}
