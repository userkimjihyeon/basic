package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthorDetailDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Integer postCount;
    private LocalDateTime createdTime;

    // 1개의 entity로만 dto가 조립되는 것이 아니기에, dto 계층에서 fromEntity를 설계
//    public static AuthorDetailDto fromEntity(Author author, Integer postCount) {
    public static AuthorDetailDto fromEntity(Author author) {
        return AuthorDetailDto.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .role(author.getRole())
//                .postCount(postCount)
                .postCount(author.getPostList().size())
                .createdTime(author.getCreatedTime())
                .build();
        // 도메인이 아니라, dto라서 this 사용불가.
        // author객체에 없는 정보들을 조회할 수도 있으므로.
        // fromEntity(Author author, Long count) 처럼 필요한거 여러개.
    }
}
