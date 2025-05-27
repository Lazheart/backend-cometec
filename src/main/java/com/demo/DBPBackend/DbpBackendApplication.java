package com.demo.DBPBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DbpBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbpBackendApplication.class, args);
	}

}
