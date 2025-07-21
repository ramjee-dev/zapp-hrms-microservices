package com.zapp.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity){

       return serverHttpSecurity.authorizeExchange(
               exchanges-> exchanges
                .pathMatchers(HttpMethod.GET).permitAll()
                .pathMatchers("/zapphrms/client-service/**").hasRole("CLIENTS")
                .pathMatchers("/zapphrms/job-service/**").hasRole("JOBS")
                .pathMatchers("/zapphrms/candidate-service/**").hasRole("CANDIDATES"))
                .oauth2ResourceServer(
                        specs->specs
                        .jwt(
                                jwtSpecs->jwtSpecs
                                        .jwtAuthenticationConverter(grantedAuthoritiesExtractor())))
               .csrf(specs->specs.disable()).build();

    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor(){

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new com.zapp.gateway_service.config.KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
