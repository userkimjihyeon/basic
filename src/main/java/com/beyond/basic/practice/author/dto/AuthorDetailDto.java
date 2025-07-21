//package com.beyond.basic.practice.author.dto;
//
//import com.beyond.basic.practice.author.domain.Author;
//import com.beyond.basic.practice.author.domain.Role;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class AuthorDetailDto {
//    private Long id;
//    private String name;
//    private String email;
//    private Role role;
//    private Integer postCount;
//    private LocalDateTime createdTime;
//
//    public static AuthorDetailDto fromEntity(Author author) {
//        return AuthorDetailDto.builder()
//                .id(author.getId())
//                .name(author.getName())
//                .email(author.getEmail())
//                .role(author.getRole())
//                .postCount(author.getPostList().size())
//                .createdTime(author.getCreatedTime())
//                .build();
//    }
//}
