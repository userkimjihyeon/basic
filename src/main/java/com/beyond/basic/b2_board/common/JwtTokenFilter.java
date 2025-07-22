package com.beyond.basic.b2_board.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends GenericFilter {
    @Value("${jwt.secretKeyAt}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;  //형변환: 사용자요청정보를 꺼내기 쉽게
        String bearerToken = req.getHeader("Authorization");   //header중 Authorization 키값
        if(bearerToken==null) {
//            token이 없는 경우 다시 filterchain으로 되돌아가는 로직
            chain.doFilter(request, response);
            return;
        }

//        token이 있는 경우 토큰검증 후 Authentication객체 생성
        String token = bearerToken.substring(7);
//        token검증 및 claims추출
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

        List<GrantedAuthority> authorities = new ArrayList<>();
//       Authentication객체를 만들때 권한은 ROLE_라는 키워드를 붙여서 만들어 주는 것이 추후 문제 발생 예방.
        authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        token이 없는 경우 다시 filterchain으로 되돌아가는 로직
        chain.doFilter(request, response);
    }
}
