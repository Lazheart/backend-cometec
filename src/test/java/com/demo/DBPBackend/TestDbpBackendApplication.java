package com.demo.DBPBackend;

import org.springframework.boot.SpringApplication;

public class TestDbpBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(DbpBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
