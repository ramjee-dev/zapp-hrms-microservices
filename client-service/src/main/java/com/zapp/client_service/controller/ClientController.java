package com.zapp.client_service.controller;

import com.zapp.client_service.dto.ClientDTO;
import com.zapp.client_service.service.IClientService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final IClientService service;

    @PostMapping
//    @PreAuthorize("hasRole('admin')")
    public ClientDTO create(@RequestBody ClientDTO dto) {
        return service.createClient(dto);
    }

    @Retry(name = "getAllClients",fallbackMethod = "getAllClientsFallback")
    @GetMapping
    public Page<ClientDTO> getAll(Pageable pageable) {
        log.debug("getALLClients API invoked");
        throw new NullPointerException("");
//        return service.getAllClients(pageable);
    }

    public Page<ClientDTO> getAllClientsFallback(Pageable pageable, Throwable throwable) {
        log.error("Fallback triggered for getAllClients due to: {}", throwable.getMessage());

        List<ClientDTO> fallbackList = Collections.emptyList(); // or pre-defined backup list
        return new PageImpl<>(fallbackList, pageable, 0);
    }


    @GetMapping("/{id}")
    public ClientDTO getById(@PathVariable Long id) {
        log.debug(" Get Client By ID API invoked");
        throw new RuntimeException("");
//        return service.getClientById(id);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('admin')")
    public ClientDTO update(@PathVariable Long id, @RequestBody ClientDTO dto) {
        return service.updateClient(id, dto);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('admin')")
    public void delete(@PathVariable Long id) {
        service.deleteClient(id);
    }
}

