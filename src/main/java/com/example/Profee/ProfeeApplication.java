package com.example.Profee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.Profee.entity")
public class ProfeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfeeApplication.class, args);
	}

}
