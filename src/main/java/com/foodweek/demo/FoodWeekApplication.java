package com.foodweek.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.foodweek.demo.repository")
public class FoodWeekApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodWeekApplication.class, args);
	}

}
