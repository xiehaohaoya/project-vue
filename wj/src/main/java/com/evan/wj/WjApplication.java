package com.evan.wj;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@MapperScan("com.evan.wj.mapper")//添加对mapper的扫描
@SpringBootApplication
public class WjApplication {

	public static void main(String[] args) {
		SpringApplication.run(WjApplication.class, args);
	}
}
