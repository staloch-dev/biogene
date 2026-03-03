package com.biogene;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BioGeneApplication {

	public static void main(String[] args) {
		SpringApplication.run(BioGeneApplication.class, args);
		System.out.println("System started successfully!");
	}

}
