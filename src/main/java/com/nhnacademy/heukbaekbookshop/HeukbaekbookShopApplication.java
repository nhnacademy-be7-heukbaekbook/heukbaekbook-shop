package com.nhnacademy.heukbaekbookshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class HeukbaekbookShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeukbaekbookShopApplication.class, args);
	}

}
