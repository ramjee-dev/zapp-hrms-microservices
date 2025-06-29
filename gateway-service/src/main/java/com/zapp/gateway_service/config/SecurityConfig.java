package com.zapp.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity){

       return serverHttpSecurity.authorizeExchange(exchanges-> exchanges
                .pathMatchers(HttpMethod.GET).permitAll()
                .pathMatchers("/zapphrms/client-service/**").authenticated()
                .pathMatchers("/zapphrms/job-service/**").authenticated()
                .pathMatchers("/zapphrms/candidate-service/**").authenticated())
                .oauth2ResourceServer(specs->specs
                        .jwt(Customizer.withDefaults()))
               .csrf(specs->specs.disable()).build();

    }
}
