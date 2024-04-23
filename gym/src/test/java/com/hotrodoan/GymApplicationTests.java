package com.hotrodoan;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class GymApplicationTests {

	@Test
	void contextLoads() {
		LocalDateTime localDateTime = LocalDateTime.now();
		System.out.println(localDateTime);
	}

}
