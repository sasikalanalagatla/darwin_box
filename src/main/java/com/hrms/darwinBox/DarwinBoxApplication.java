package com.hrms.darwinBox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DarwinBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(DarwinBoxApplication.class, args);
	}

}
