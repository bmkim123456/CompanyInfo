package com.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CompanyInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyInfoApplication.class, args);
	}

}
