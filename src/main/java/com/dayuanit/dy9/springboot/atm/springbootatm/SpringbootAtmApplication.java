package com.dayuanit.dy9.springboot.atm.springbootatm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.dayuanit.dy9.springboot.atm.springbootatm.mapper")
@EnableScheduling
//@EnableAsync  支持并发操作
public class SpringbootAtmApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootAtmApplication.class, args);
	}
}
