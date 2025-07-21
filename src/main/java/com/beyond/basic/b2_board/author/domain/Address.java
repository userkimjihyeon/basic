package com.beyond.basic.b2_board.author.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
@Entity
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String street;
    private String zipCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", unique = true) //Address 테이블에 name이 author_id라는 fk컬럼이 생성된다
    private Author author;
}
