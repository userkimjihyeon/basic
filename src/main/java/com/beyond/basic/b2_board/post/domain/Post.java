package com.beyond.basic.b2_board.post.domain;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    @Column(nullable = false)
    private String title;
    @Column(length = 3000)
    private String contents;
    @Builder.Default
    private String delYn = "N";
    @Builder.Default
    private String appointment = "N";
    private LocalDateTime appoinmentTime;

//    ⭐fk설정시 ManyToOne필수 (fk거는쪽에서 해당어노테이션 달아야함. OneToOne 등)
//    ManyToOne에서는 default가 fetch.EAGER(즉시로딩) :author객체를 사용하지 않아도 author테이블로 쿼리발생 (-> 성능이슈)
//    그래서, 일반적으로 fetch.LAZY(지연로딩) 설정 :author객체를 사용하지않는 한 author테이블로 쿼리발생X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id") //⭐fk관계설정 //Post 테이블에 name이 author_id라는 fk컬럼이 생성된다
    private Author author;

    public void updateAppointment(String appointment) {
        this.appointment = appointment;
    }
}
