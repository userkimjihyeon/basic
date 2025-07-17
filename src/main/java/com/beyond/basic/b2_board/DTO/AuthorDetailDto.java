package com.beyond.basic.b2_board.Dto;

import com.beyond.basic.b2_board.Domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AuthorDetailDto {
    private Long id;
    private String name;
    private String password;

    // 1개의 entity로만 dto가 조립되는 것이 아니기에, dto 계층에서 fromEntity를 설계
    public static AuthorDetailDto fromEntity(Author author) {
        return new AuthorDetailDto(author.getId(), author.getName(), author.getPassword());
        // 도메인이 아니라, dto라서 this 사용불가.
        // author객체에 없는 정보들을 조회할 수도 있으므로.
        // fromEntity(Author author, Long count) 처럼 필요한거 여러개.
    }
}
