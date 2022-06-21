package com.school.kiqa;

import com.school.kiqa.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.RequestBody;

@EnableAsync(proxyTargetClass = true)
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class KiqaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KiqaApplication.class, args);
	}

}
