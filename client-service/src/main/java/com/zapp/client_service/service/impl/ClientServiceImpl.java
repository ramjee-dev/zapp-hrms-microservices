package com.zapp.client_service.service.impl;

import com.zapp.client_service.dto.ClientDTO;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.repository.ClientRepository;
import com.zapp.client_service.service.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final ClientRepository repository;

    @Override
    public ClientDTO createClient(ClientDTO dto) {
        if (repository.existsByName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client name already exists");
        }
        Client client = Client.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .status(dto.getStatus() != null ? dto.getStatus() : Client.Status.ACTIVE)
                .build();
        repository.save(client);
        dto.setId(client.getId());
        return dto;
    }

    @Override
    public Page<ClientDTO> getAllClients(Pageable pageable) {
        return repository.findAll(pageable)
                .map(c -> new ClientDTO(c.getId(), c.getName(), c.getLocation(), c.getStatus()));
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client c = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        return new ClientDTO(c.getId(), c.getName(), c.getLocation(), c.getStatus());
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO dto) {
        Client existing = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        existing.setName(dto.getName());
        existing.setLocation(dto.getLocation());
        existing.setStatus(dto.getStatus());

        repository.save(existing);
        return new ClientDTO(existing.getId(), existing.getName(), existing.getLocation(), existing.getStatus());
    }

    @Override
    public void deleteClient(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
        repository.deleteById(id);
    }

}
