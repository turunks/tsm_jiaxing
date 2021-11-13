package com.heyue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)   //开启事物管理功能
@MapperScan({"cn.com.heyue.mapper"})
public class TsmApplication {

	public static void main(String[] args) {
		SpringApplication.run(TsmApplication.class, args);
	}
}
