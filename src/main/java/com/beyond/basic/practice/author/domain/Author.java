package com.beyond.basic.practice.author.domain;

import com.beyond.basic.practice.author.dto.AuthorListDto;
import com.beyond.basic.practice.common.BaseTimeEntity;
import com.beyond.basic.practice.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
public class Author extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @Builder.Default
    List<Post> postList = new ArrayList<>();

    public void updatePw(String password) {
        this.password = password;
    }
    public AuthorListDto listFromEntity() {
        return new AuthorListDto(this.id, this.name, this.email);
    }
}
