package com.zapp.gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	@Bean
	public RouteLocator ZappHrmsRouteConfig(RouteLocatorBuilder builder){

		return builder.routes()
				.route(p->p
						.path("/zapphrms/client-service/**")
						.filters(f->f.rewritePath("/zapphrms/client-service/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.retry(config->config
										.setRetries(3)
										.setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true)
										.setMethods(HttpMethod.GET)))
						.uri("lb://CLIENT-SERVICE"))
				.route(p->p
						.path("/zapphrms/job-service/**")
						.filters(f->f.rewritePath("/zapphrms/job-service/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config->config.setName("jobsCircuitBreaker").setFallbackUri("forward:/fallback/job-service")))
						.uri("lb://JOB-SERVICE"))
				.route(p->p
						.path("/zapphrms/candidate-service/**")
						.filters(f->f.rewritePath("/zapphrms/candidate-service/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://CANDIDATE-SERVICE"))
				.build();

	}

}
