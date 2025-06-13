package com.zapp.client_service.service;

import com.zapp.client_service.dto.ClientDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClientService {
    ClientDTO createClient(ClientDTO dto);
    Page<ClientDTO> getAllClients(Pageable pageable);
    ClientDTO getClientById(Long id);
    ClientDTO updateClient(Long id, ClientDTO dto);
    void deleteClient(Long id);
}
