package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateDto {
    @NotEmpty
    private String title;
    private String contents;
    @NotNull    //숫자는 @NotEmpty 사용불가
    private Long authorId;

    public Post toEntity(Author author) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
//                .authorId(this.authorId)  //case1. 관계성 설정 전
                .author(author)
                .delYn("N")
                .build();
    }
}
