package com.beyond.basic.b2_board.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
//@PreAuthorize 어노테이션을 사용하기 위한 설정
@EnableMethodSecurity
//로그인관련 설정정보. (코드암기X의미알기O)
public class SecurityConfig {
//    내가 만든 객체는 @Component, 외부 라이브러리를 활용한 객체는 @Bean+@Configuation
//    @Bean은 메서드 위에 붙여 Return되는 객체를 싱글톤 객체로 생성. Conponent는 클래스 위에 붙여 클래스 자체를 싱글톤 객체로 생성.
//    filter계층에서 filter로직을 커스텀한다.
    private final JwtTokenFilter jwtTokenFilter;
    private final JwtAuthenticationHandler jwtAuthenticationHandler;
    private final JwtAuthorizationHandler jwtAuthorizationHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
//                cors: 특정도메인에 대한 허용정책, postman은 cors정책에 적용X
                .cors(c -> c.configurationSource(corsConfiguration()))
//                csrf(보안공격중하나로서 타 사이트의 쿠키값을 꺼내서 탈취하는 공격) 비활성화
//                세션기반 로그인(mvc패턴, ssr)에서는 csrf별도 설정하는 것이 일반적
//                토큰기반 로그인(rest api서버, csr)에서는 csrf설정하지 않는 것이 일반적   -> 토큰로그인할거니까csrf신경ㄴㄴ
                .csrf(AbstractHttpConfigurer::disable)
//                httpBasic은 email/pw를 인코딩하여 인증(전송)하는 방식. 간단한 인증의 경우에만 사용.(보안취약)
                .httpBasic(AbstractHttpConfigurer::disable)
//                세션로그인방식 비활성화
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                ⭐아래두개중요
//                token을 검증하고, token검증을 통해 Authentication객체생성
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(jwtAuthenticationHandler)    //401의 경우(인증) -> authenticaton객체가없음!
                         .accessDeniedHandler(jwtAuthorizationHandler)  //403의 경우(인가, 권한)   -> authentication객체가 잘못됨!
                )
//                예외 api 정책 설정
//                .authenticated(): 예외를 제외한 모든 요청에 대해서 Authentication객체가 생성되기를 요구
                .authorizeHttpRequests(a -> a.requestMatchers("/author/create", "/author/doLogin").permitAll().anyRequest().authenticated())
                .build();
    }
    private CorsConfigurationSource corsConfiguration(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 HTTP(get, post 등) 메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더요소(Authorization 등) 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //모든 url패턴에 대해 cors설정 적용
        return source;
    }
//    PasswordEncoder 빈을 등록하는 이유는 사용자 비밀번호를 안전하게 인코딩(해시)하고, 로그인할 때 저장된 해시와 비교하기 위해
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
