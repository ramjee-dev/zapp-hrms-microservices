package com.zapp.job_service.service.client;
import com.zapp.job_service.dto.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("client-service")
public interface ClientServiceFeignClient {

    @GetMapping(value = "/clients/{id}", consumes = "application/json")
    ResponseEntity<ClientDto> getClientById(@PathVariable("id") Long id);
}

