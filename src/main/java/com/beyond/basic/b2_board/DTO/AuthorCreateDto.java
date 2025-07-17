package com.beyond.basic.b2_board.Dto;

import com.beyond.basic.b2_board.Domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data // Dto 계층은 데이터의 안정성이 엔티티만큼 중요하지는 않으므로, setter도 일반적으로 추가.

public class AuthorCreateDto {
    private String name;
    private String email;
    private String password;
    // id같은건 필요없음

    // to 엔티티는 DTO에, from엔티티는 객체에 하는것
    public Author authorToEntity(){
        return new Author(this.name, this.email, this.password);
    }
}
