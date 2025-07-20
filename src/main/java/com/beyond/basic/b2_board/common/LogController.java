package com.beyond.basic.b2_board.common;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//logback 객체 만드는 방법2.
@Slf4j
@RestController
public class LogController {
////    logback 객체 만드는 방법1. (import는 slf4j로 함)
//    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @GetMapping("/log/test")
    public String logTest() {
//        기존의 system print는 spring에서는 잘 사용되지 않음.
//        이유 : 1)성능이 떨어짐 2)로그분류작업 불가
        System.out.println("hello world");

////        가장많이 사용되는 로그 라이브러리 : logback
////        로그레벨(프로젝트차원의 설정) : trace < debug < info < error
//        logger.trace("trace 로그 입니다.");
//        logger.debug("debug 로그 입니다.");  //로그가 info를 제외한 debug 이하만 찍힘
//        logger.info("info 로그 입니다.");    //디폴트
//        logger.error("error 로그 입니다.");

//        @Slf4j 어노테이션을 선언시, log라는 변수로 logback객체 사용가능
        log.info("slf4j 테스트");

        try {
            log.info("에러테스트 시작");
        } catch (RuntimeException e) {
//            +보다 ,로 두 메세지를 이어서 출력하는 것이 성능에 더 좋음.
            log.error("에러메시지 : ", e);
            e.printStackTrace();    //성능문제로 logback을 쓰는게 더 좋음.
        }
        return "ok";
    }
}
