package com.zapp.client_service;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Client-Service REST API Documentation",
				description = "Zapp Hrms Client microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Ramjee",
						email = "ramjeedev.tech@gmail.com",
						url = ""
				),license = @License(
						name = "Apache 2.0",
				url = ""
		)
		),
		externalDocs =@ExternalDocumentation(
				description = "Zapp Hrms Client microservice REST API Documentation",
				url = ""
		)
)
public class ClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
	}

}
