package com.school.kiqa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync(proxyTargetClass = true)
@SpringBootApplication
public class KiqaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KiqaApplication.class, args);
	}

}
