package com.beyond.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//SpringBootApplication : Controller의 component를 읽는 ComponentScan를 포함
//ComponentScan은 Application파일을 포함한 경로 하위의 요소들만 scan가능
//본 파일 경로이동 금지
@SpringBootApplication
public class BasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicApplication.class, args);
	}

}
