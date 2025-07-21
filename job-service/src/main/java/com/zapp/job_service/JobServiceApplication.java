package com.zapp.job_service;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(
		info = @Info(
				title = "Job-Service REST API Documentation",
				description = "Zapp Hrms Job microservice REST API Documentation",
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
				description = "Zapp Hrms Job microservice REST API Documentation",
				url = "github.com/ramjee-dev"
		)
)
public class JobServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobServiceApplication.class, args);
	}

}
