package com.interview.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SubscriptionApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionApiApplication.class, args);
	}

}
