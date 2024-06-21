package com.hotrodoan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class GymApplication {
	public static void main(String[] args) {
		SpringApplication.run(GymApplication.class, args);
	}

}
