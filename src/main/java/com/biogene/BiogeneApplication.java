package com.biogene;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BiogeneApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(BiogeneApplication.class, args);
		String port = context.getEnvironment().getProperty("server.port", "8080");
		System.out.println("\n~~~ System started successfully! ~~~");
		System.out.println("Swagger UI: http://localhost:" + port + "/swagger-ui.html");
		System.out.println("--------------------------------------------\n");
	}

}
