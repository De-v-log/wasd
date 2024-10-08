package com.wasd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class WasdApplication {

	public static void main(String[] args) {
		SpringApplication.run(WasdApplication.class, args);
	}

}
