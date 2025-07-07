package com.example.tdgameserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.tdgameserver.mapper")
public class TdGameServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TdGameServerApplication.class, args);
	}

}
