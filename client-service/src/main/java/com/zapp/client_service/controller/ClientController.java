package com.zapp.client_service.controller;

import com.zapp.client_service.dto.ClientDTO;
import com.zapp.client_service.service.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final IClientService service;

    @PostMapping
//    @PreAuthorize("hasRole('admin')")
    public ClientDTO create(@RequestBody ClientDTO dto) {
        return service.createClient(dto);
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('admin', 'bd', 'tat')")
    public Page<ClientDTO> getAll(Pageable pageable) {
        return service.getAllClients(pageable);
    }

    @GetMapping("/{id}")

    public ClientDTO getById(@PathVariable Long id) {
        return service.getClientById(id);
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

