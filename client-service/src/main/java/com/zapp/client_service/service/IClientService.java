package com.zapp.client_service.service;

import com.zapp.client_service.dto.*;
import com.zapp.client_service.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClientService {

    ClientResponseDto createClient(ClientCreateRequestDto dto);

    ClientResponseDto fetchClientById(Long clientId);

    ClientPageResponseDto getAllClients(ClientPageRequestDto dto);

    List<ClientResponseDto> getClientByStatus(Client.Status status);

    ClientResponseDto updateClient(Long clientId, ClientUpdateRequestDto clientDto);

    ClientResponseDto partialUpdateClient(Long clientId, ClientPartialUpdateRequestDto clientDto);

    void deleteClient(Long clientId);
}
