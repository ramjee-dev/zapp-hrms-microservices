package com.zapp.candidate_service;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Candidate-Service REST API Documentation",
				description = "Zapp Hrms Candidate microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Ramjee",
						email = "ramjeedev.tech@gmail.com",
						url = "github.com/ramjee-dev"
				),license = @License(
				name = "Apache 2.0",
				url = "github.com/ramjee-dev"
		)
		),
		externalDocs =@ExternalDocumentation(
				description = "Zapp Hrms Candidate microservice REST API Documentation",
				url = "github.com/ramjee-dev"
		)
)
public class CandidateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CandidateServiceApplication.class, args);
	}

}
