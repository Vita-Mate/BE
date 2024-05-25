package com.example.vitamate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VitamateApplication {

	public static void main(String[] args) {
		SpringApplication.run(VitamateApplication.class, args);
	}

}
