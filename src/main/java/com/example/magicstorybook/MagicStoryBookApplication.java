package com.example.magicstorybook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.magicstorybook.repository")
@EntityScan(basePackages = "com.example.magicstorybook.model")
public class MagicStoryBookApplication {

	public static void main(String[] args) {

		SpringApplication.run(MagicStoryBookApplication.class, args);
	}

}
