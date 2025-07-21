//package com.beyond.basic.practice.common;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//public class LogController {
//    @GetMapping("/log/test")
//    public String logTest() {
//        log.info("slf4j 테스트");
//
//        try {
//            log.info("에러테스트 시작");
//        } catch (RuntimeException e) {
//            log.error("에러메세지 : ", e);
//        }
//        return "ok";
//    }
//}
