package com.hotrodoan;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@CrossOrigin(origins = "*")
public class GymApplication {
	// @Bean
	// FirebaseMessaging firebaseMessaging() throws IOException {
	// 	GoogleCredentials googleCredentials = GoogleCredentials
	// 			.fromStream(new ClassPathResource("firebase-service-notification.json").getInputStream());

	// 	FirebaseOptions firebaseOptions = FirebaseOptions.builder()
	// 			.setCredentials(googleCredentials).build();

	// 	FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "gym-app");
	// 	return FirebaseMessaging.getInstance(app);
	// }

	public static void main(String[] args) {
		SpringApplication.run(GymApplication.class, args);
	}

}
