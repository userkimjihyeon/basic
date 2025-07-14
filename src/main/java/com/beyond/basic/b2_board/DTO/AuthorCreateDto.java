package com.beyond.basic.b2_board.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   //dto계층은 데이터의 안정성이 엔티티(도메인)만큼 중요하지는 않으므로, setter도 일반적으로 추가.
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCreateDto {
//    Author객체에서 id, 생성일시 불필요.
    private String name;
    private String email;
    private String password;
}
