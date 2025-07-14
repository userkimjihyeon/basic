package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter //@Data(getter+setter)는 사용안함!
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private Long id;
    private String name;
    private String email;
    private String password;

//    id를 제외한 생성자
    public Author(String name, String email, String password) {
        this.id = AuthorMemoryRepository.id;    // -> public static Long id = 1L;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public void updatePw(String newPassword) {
        this.password = newPassword;
    }
}
